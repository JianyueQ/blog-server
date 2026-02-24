package com.blog.business.websocket;

import com.alibaba.fastjson2.JSON;
import com.blog.business.domain.dto.MessageReadStatusDto;
import com.blog.business.domain.entity.MessageRecord;
import com.blog.business.domain.vo.MessageReadStatusVo;
import com.blog.business.domain.vo.MessageRecordVo;
import com.blog.business.service.MessageRecordService;
import com.blog.common.constant.CacheConstants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.exception.ServiceException;
import com.blog.common.exception.user.UserException;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.spring.SpringUtils;
import com.blog.framework.websocket.handler.BaseWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.blog.framework.websocket.interceptor.AuthHandshakeInterceptor.*;

/**
 * 消息记录处理器
 *
 * @author 31373
 */
@Component
public class MessageRecordWebsocketHandler extends BaseWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageRecordWebsocketHandler.class);

    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = 1;
    /**
     * 是否已读 1-已读
     */
    private static final Integer IS_READ_Y = 1;

    /**
     * 鉴权时间 60秒
     */
    protected long authorizationTimeout = 60000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 构造方法 需要传入核心线程数
     */
    public MessageRecordWebsocketHandler() {
        super(CORE_POOL_SIZE);
        // 初始化自定义任务
        initializeCustomTasks();
    }

    /**
     * 给ws在线的管理员发送信息
     */
    public void sendMessageToOnlineAdmins(MessageRecord messageRecord) {
        MessageRecordVo messageRecordVo = MessageRecordVo.builder()
                .messageId(messageRecord.getMessageId())
                .messageTitle(messageRecord.getMessageTitle())
                .messageContent(messageRecord.getMessageContent())
                .messageType(messageRecord.getMessageType())
                .isRead(messageRecord.getIsRead())
                .createTime(messageRecord.getCreateTime().toString())
                .build();
        String jsonData = JSON.toJSONString(messageRecordVo);
        // 查找并发送消息给对应的管理员WebSocket连接
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    // 获取会话中的管理员ID
                    Long sessionAdminId = (Long) session.getAttributes().get(ADMIN_ID);

                    // 匹配特定管理员ID发送消息
                    if (sessionAdminId.equals(messageRecord.getAdminId())) {
                        sendMessage(session, jsonData);
                    }
                }
            } catch (Exception e) {
                log.error("向管理员发送消息失败: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 初始化自定义任务
     */
    @Override
    protected void initializeCustomTasks() {
        // 启动定期鉴权
        startRegularAuthorization();
    }

    /**
     * 配置功能
     */
    @Override
    protected void configureFeatures() {

    }

    /**
     * 连接建立时调用
     */
    @Override
    protected void onConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.info("管理员 {} 建立服务器监控连接", username);
    }

    /**
     * 处理自定义消息
     */
    @Override
    protected void handleCustomMessage(WebSocketSession session, String message) throws Exception {
        //处理消息状态的修改
        MessageReadStatusDto messageReadStatusDto = JSON.parseObject(message, MessageReadStatusDto.class);
        MessageReadStatusVo messageReadStatusVo = SpringUtils.getBean(MessageRecordService.class).updateMessageReadStatus(messageReadStatusDto);
        AjaxResult ajax = messageReadStatusVo != null ? AjaxResult.success(messageReadStatusVo) : AjaxResult.error("请求参数异常");
        String jsonData = JSON.toJSONString(ajax);
        sendMessage(session,jsonData);
    }

    /**
     * 连接关闭时调用
     */
    @Override
    protected void onConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.info("管理员 {} 断开服务器监控连接, 状态: {}", username, status);
    }

    /**
     * 处理传输错误
     */
    @Override
    protected void onTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String username = (String) session.getAttributes().get(CURRENT_USER);
        log.error("管理员 {} WebSocket传输错误: {}", username, exception.getMessage(), exception);
    }

    /**
     * 销毁时调用
     */
    @Override
    protected void onDestroy() {
        log.info("消息发送服务WebSocket服务正在关闭");
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
                    }
                }
            } catch (Exception e) {
                log.error("检查用户认证状态时出错", e);
            }
        }
    }
}
