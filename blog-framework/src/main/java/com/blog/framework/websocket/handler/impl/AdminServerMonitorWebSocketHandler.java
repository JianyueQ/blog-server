package com.blog.framework.websocket.handler.impl;

import com.alibaba.fastjson2.JSON;
import com.blog.common.constant.CacheConstants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.StringUtils;
import com.blog.framework.web.domain.Server;
import com.blog.framework.web.service.TokenService;
import com.blog.framework.websocket.handler.BaseWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.blog.framework.websocket.interceptor.AuthHandshakeInterceptor.*;

/**
 * 服务器监控WebSocket处理器 - 管理端
 *
 * @author 31373
 */
@Component
public class AdminServerMonitorWebSocketHandler extends BaseWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(AdminServerMonitorWebSocketHandler.class);

    /**
     * 鉴权时间 60秒
     */
    protected long authorizationTimeout = 60000L;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisCache redisCache;

    public AdminServerMonitorWebSocketHandler() {
        // 设置核心线程数为2
        super(2);
        initializeCustomTasks();
    }

    public void setAuthorizationTimeout(long authorizationTimeout) {
        this.authorizationTimeout = authorizationTimeout;
    }

    /**
     * 初始化自定义任务
     */
    @Override
    protected void initializeCustomTasks() {
        // 启动监控任务
        startMonitoringServer();
        // 启动定期鉴权
        startRegularAuthorization();
    }

    /**
     * 配置自定义功能
     */
    @Override
    protected void configureFeatures() {

    }

    /**
     * 处理连接建立事件
     *
     * @param session WebSocket会话
     * @throws Exception
     */
    @Override
    protected void onConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.info("管理员 {} 建立服务器监控连接", username);

        // 发送详细的服务器信息
        sendServerInfo(session);
    }

    /**
     * 处理自定义消息
     *
     * @param session WebSocket会话
     * @param message 消息内容
     * @throws Exception
     */
    @Override
    protected void handleCustomMessage(WebSocketSession session, String message) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.warn("管理员 {} 发送未知消息: {}", username, message);

        sendMessage(session, JSON.toJSONString(Map.of(
                TYPE, "error",
                MESSAGE, "未知消息类型: " + message
        )));
    }

    @Override
    protected void handleRefresh(WebSocketSession session) throws Exception {
        sendServerInfo(session);
    }

    /**
     * 处理连接关闭事件
     *
     * @param session WebSocket会话
     * @param status  关闭状态
     * @throws Exception
     */
    @Override
    protected void onConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.info("管理员 {} 断开服务器监控连接, 状态: {}", username, status);
    }

    /**
     * 处理传输错误事件
     *
     * @param session   WebSocket会话
     * @param exception 异常信息
     * @throws Exception
     */
    @Override
    protected void onTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.error("管理员 {} WebSocket传输错误: {}", username, exception.getMessage(), exception);
    }

    /**
     * 处理销毁事件
     */
    @Override
    protected void onDestroy() {
        log.info("服务器监控WebSocket服务正在关闭");
    }

    /**
     * 发送服务器详细信息
     */
    private void sendServerInfo(WebSocketSession session) throws Exception {
        Server server = new Server();
        server.copyTo();
        String jsonData = JSON.toJSONString(Map.of(
                TYPE, "server_info",
                DATA, server,
                TIMESTAMP, System.currentTimeMillis()
        ));
        sendMessage(session, jsonData);
    }

    /**
     * 启动监控任务
     */
    private void startMonitoringServer() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                broadcastServerInfo();
            } catch (Exception e) {
                log.error("WebSocket监控任务执行异常", e);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    /**
     * 广播服务器信息
     */
    private void broadcastServerInfo() {
        Server server = new Server();
        try {
            server.copyTo();
            String jsonData = JSON.toJSONString(Map.of(
                    TYPE, "server_info",
                    DATA, server,
                    TIMESTAMP, System.currentTimeMillis()
            ));

            sessions.removeIf(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(jsonData));
                        return false;
                    }
                } catch (Exception e) {
                    log.error("向用户 {} 发送WebSocket消息失败",
                            session.getAttributes().get(CURRENT_USER), e);
                }
                return true;
            });
        } catch (Exception e) {
            log.error("获取服务器信息失败", e);
        }
    }

    /**
     * 启用定期鉴权
     */
    private void startRegularAuthorization() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                authorizeConnections();
            } catch (Exception e) {
                log.error("定期鉴权任务执行异常", e);
            }
        }, authorizationTimeout, authorizationTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 检查用户认证状态
     */
    private void authorizeConnections() {
        HashSet<WebSocketSession> sessionsToAuthorize = new HashSet<>(sessions);
        for (WebSocketSession session : sessionsToAuthorize) {
            try {
                String uuid = (String) session.getAttributes().get(UUID);
                Long adminId = (Long) session.getAttributes().get(ADMIN_ID);
                String username = (String) session.getAttributes().get(CURRENT_USER);
                if (StringUtils.isNotEmpty(uuid)) {
                    String redisKey = CacheConstants.LOGIN_TOKEN_KEY + uuid;
                    // 验证Redis中用户信息是否仍然有效
                    LoginUserOnAdmin user = redisCache.getCacheObject(redisKey);
                    if (StringUtils.isNull(user)) {
                        log.warn("用户 {}(ID:{}) 认证已过期，强制断开连接", username, adminId);
                        sendMessage(session, JSON.toJSONString(Map.of(
                                "type", "error",
                                "message", "认证已过期"
                        )));
                        session.close(CloseStatus.GOING_AWAY.withReason("认证已过期"));
                    } else {
                        log.debug("用户 {}(ID:{}) 认证有效,进行刷新令牌操作", username, adminId);
                        tokenService.verifyToken(user);
                    }
                }
            } catch (Exception e) {
                log.error("检查用户认证状态时出错", e);
            }
        }
    }


}
