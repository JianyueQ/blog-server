package com.blog.framework.websocket.handler;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 基础的WebSocket处理器
 *
 * @author 31373
 */
public abstract class BaseWebSocketHandler extends TextWebSocketHandler {

    protected static final Logger log = LoggerFactory.getLogger(BaseWebSocketHandler.class);
    /**
     * 心跳消息，用于检测连接是否正常
     */
    protected static final String PING_MESSAGE = "ping";
    /**
     * 心跳响应消息，用于响应心跳检测
     */
    protected static final String PONG_MESSAGE = "pong";
    /**
     * 刷新消息，用于强制客户端刷新
     */
    protected static final String REFRESH_MESSAGE = "refresh";
    /**
     * 状态消息，用于获取连接状态
     */
    protected static final String STATUS_MESSAGE = "status";
    /**
     * 连接ID，用于标识连接
     */
    protected static final String CONNECTION_ID = "connectionId";
    /**
     * 最后心跳时间，用于记录最后心跳时间
     */
    protected static final String LAST_HEARTBEAT = "lastHeartbeat";
    /**
     * 连接时间，用于记录连接时间
     */
    protected static final String CONNECTION_TIME = "connectionTime";
    /**
     * 时间自上次心跳，用于记录时间自上次心跳
     */
    protected static final String TIME_SINCE_LAST_HEARTBEAT = "timeSinceLastHeartbeat";
    /**
     * 类型，用于标识消息类型
     */
    protected static final String TYPE = "type";
    /**
     * 数据，用于存储消息数据
     */
    protected static final String DATA = "data";
    /**
     * 特征，用于存储功能特性
     */
    protected static final String FEATURES = "features";
    /**
     * 消息，用于存储消息内容
     */
    protected static final String MESSAGE = "message";
    /**
     * 时间戳，用于记录消息时间
     */
    protected static final String TIMESTAMP = "timestamp";
    /**
     * 连接集合，用于存储所有连接
     */
    protected final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    /**
     * 核心线程数，用于配置线程池
     */
    protected final int corePoolSize;
    /**
     * 调度器，用于执行定时任务
     */
    protected ScheduledExecutorService scheduler;
    /**
     * 心跳间隔 10秒
     */
    protected long heartbeatInterval = 10000L;
    /**
     * 心跳超时 25秒
     */
    protected long heartbeatTimeout = 25000L;
    /**
     * 是否启用心跳检测
     */
    protected boolean enableHeartbeat = true;

    /**
     * 构造方法 需要传入核心线程数
     */
    public BaseWebSocketHandler(int corePoolSize) {
        configureFeatures();
        // 基础心跳任务
        int baseTasks = enableHeartbeat ? 1 : 0;
        this.corePoolSize = getSuggestedThreadPoolSize(baseTasks, corePoolSize);
        this.scheduler = createScheduler();
        initializeTasks();
    }

    /**
     * 创建调度器
     *
     * @return ScheduledExecutorService
     */
    protected ScheduledExecutorService createScheduler() {
        // 默认创建1个核心线程的调度器
        return Executors.newScheduledThreadPool(corePoolSize);
    }

    /**
     * 获取建议的线程池大小
     */
    protected int getSuggestedThreadPoolSize(int baseTasks, int customTasks) {
        return Math.max(baseTasks + customTasks, 1);
    }

    /**
     * 初始化任务调度
     */
    private void initializeTasks() {
        // 初始化心跳检测任务
        if (enableHeartbeat) {
            startHeartbeatChecker();
        }
    }

    /**
     * 初始化子类自定义任务 - 子类重写此方法来启动自己的调度任务
     */
    protected abstract void initializeCustomTasks();

    /**
     * 参数配置 - 子类重写以配置功能开关和参数
     */
    protected abstract void configureFeatures();

    // === 生命周期方法 - 子类可重写 ===

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("建立WebSocket连接: {}", getSessionInfo(session));

        // 初始化连接属性
        initializeSessionAttributes(session);

        sessions.add(session);

        // 发送连接确认
        sendConnectedMessage(session);

        // 执行业务特定的连接建立逻辑
        onConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
//        log.debug("收到消息: {} from {}", payload, getSessionInfo(session));

        switch (payload.toLowerCase()) {
            // 处理ping消息
            case PING_MESSAGE -> handlePing(session);
            // 处理pong消息
            case PONG_MESSAGE -> handlePong(session);
            // 处理刷新请求
            case REFRESH_MESSAGE -> handleRefresh(session);
            // 处理状态请求
            case STATUS_MESSAGE -> handleStatus(session);
            // 处理自定义消息
            default -> handleCustomMessage(session, payload);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("连接关闭: {} Status: {}", getSessionInfo(session), status);
        onConnectionClosed(session, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("传输错误: {} Error: {}", getSessionInfo(session), exception.getMessage(), exception);
        sessions.remove(session);
        onTransportError(session, exception);
    }

    @PreDestroy
    public void destroy() {
        closeAllConnections();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        onDestroy();
    }

    /**
     * 连接建立时的业务逻辑
     */
    protected abstract void onConnectionEstablished(WebSocketSession session) throws Exception;

    /**
     * 处理自定义业务消息
     */
    protected abstract void handleCustomMessage(WebSocketSession session, String message) throws Exception;

    /**
     * 连接关闭时的清理工作
     */
    protected abstract void onConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception;

    /**
     * 传输错误处理
     */
    protected abstract void onTransportError(WebSocketSession session, Throwable exception) throws Exception;

    /**
     * 销毁时的清理工作
     */
    protected abstract void onDestroy();

    /**
     * 初始化会话属性
     */
    protected void initializeSessionAttributes(WebSocketSession session) {
        session.getAttributes().put(LAST_HEARTBEAT, System.currentTimeMillis());
        session.getAttributes().put(CONNECTION_TIME, System.currentTimeMillis());
        session.getAttributes().put(CONNECTION_ID, generateConnectionId());
    }

    /**
     * 发送连接确认消息
     */
    protected void sendConnectedMessage(WebSocketSession session) throws IOException {
        sendMessage(session, JSON.toJSONString(Map.of(
                TYPE, "connected",
                MESSAGE, "WebSocket连接已建立",
                FEATURES, Map.of(
                        "heartbeat", enableHeartbeat
                )
        )));
    }

    /**
     * 处理ping消息
     */
    protected void handlePing(WebSocketSession session) throws IOException {
        sendMessage(session, PONG_MESSAGE);
        updateHeartbeat(session);
    }

    /**
     * 处理pong消息
     */
    protected void handlePong(WebSocketSession session) {
        updateHeartbeat(session);
    }

    /**
     * 处理刷新请求
     */
    protected void handleRefresh(WebSocketSession session) throws Exception {
        // 子类实现具体的刷新逻辑
    }

    /**
     * 处理状态查询
     */
    protected void handleStatus(WebSocketSession session) throws IOException {
        Long lastHeartbeat = (Long) session.getAttributes().get(LAST_HEARTBEAT);
        long currentTime = System.currentTimeMillis();
        long timeSinceLastHeartbeat = currentTime - (lastHeartbeat != null ? lastHeartbeat : currentTime);
        if (lastHeartbeat == null) {
            lastHeartbeat = (Long) session.getAttributes().get(CONNECTION_TIME);
        }
        String jsonData = JSON.toJSONString(Map.of(
                TYPE, "connection_status",
                DATA, Map.of(
                        CONNECTION_ID, session.getAttributes().get(CONNECTION_ID),
                        LAST_HEARTBEAT, lastHeartbeat,
                        TIME_SINCE_LAST_HEARTBEAT, timeSinceLastHeartbeat,
                        FEATURES, Map.of(
                                "heartbeat", enableHeartbeat
                        )
                ),
                TIMESTAMP, currentTime
        ));
        sendMessage(session, jsonData);
    }

    /**
     * 更新心跳时间
     */
    protected void updateHeartbeat(WebSocketSession session) {
        session.getAttributes().put(LAST_HEARTBEAT, System.currentTimeMillis());
    }

    /**
     * 生成连接ID
     */
    protected String generateConnectionId() {
        return getClass().getSimpleName() + "_" + System.currentTimeMillis() + "_" +
                ThreadLocalRandom.current().nextInt(1000);
    }

    /**
     * 获取会话信息用于日志
     */
    protected String getSessionInfo(WebSocketSession session) {
        return String.format("Session[%s]", session.getAttributes().get(CONNECTION_ID));
    }

    /**
     * 统一的消息发送方法
     */
    protected void sendMessage(WebSocketSession session, String message) throws IOException {
        if (session.isOpen()) {
            synchronized (session) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    /**
     * 关闭所有连接
     */
    protected void closeAllConnections() {
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.close(CloseStatus.GOING_AWAY.withReason("服务器关闭"));
                }
            } catch (Exception e) {
                log.error("关闭连接时出错", e);
            }
        });
        sessions.clear();
    }

    /**
     * 启动心跳检测任务
     */
    protected void startHeartbeatChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkHeartbeats();
            } catch (Exception e) {
                log.error("心跳检测任务执行异常", e);
            }
        }, heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * 心跳检测逻辑
     */
    protected void checkHeartbeats() {
        long currentTime = System.currentTimeMillis();
        Set<WebSocketSession> sessionsToCheck = new HashSet<>(sessions);

        for (WebSocketSession session : sessionsToCheck) {
            try {
                Long lastHeartbeat = (Long) session.getAttributes().get(LAST_HEARTBEAT);
                if (lastHeartbeat == null) {
                    lastHeartbeat = (Long) session.getAttributes().get(CONNECTION_TIME);
                }
                // 判断是否超时
                if (lastHeartbeat != null && (currentTime - lastHeartbeat) > heartbeatTimeout) {
                    log.warn("连接超时，断开连接: {}", getSessionInfo(session));
                    sessions.remove(session);
                    if (session.isOpen()) {
                        session.close(CloseStatus.GOING_AWAY.withReason("心跳超时"));
                    }
                }
                // 发送ping消息
                if (session.isOpen() && enableHeartbeat) {
                    sendMessage(session, PING_MESSAGE);
                }
            } catch (Exception e) {
                log.error("检查连接心跳时出错", e);
                sessions.remove(session);
            }
        }
    }

    public void setHeartbeatInterval(long heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public void setHeartbeatTimeout(long heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public void setEnableHeartbeat(boolean enableHeartbeat) {
        this.enableHeartbeat = enableHeartbeat;
    }
}
