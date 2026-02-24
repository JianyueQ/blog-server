package com.blog.business.config;

import com.blog.business.websocket.MessageRecordWebsocketHandler;
import com.blog.framework.websocket.interceptor.AuthHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author 31373
 */
@Configuration(value = "BusinessWebsocketConfig")
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebsocketConfig.class);

    @Autowired
    private MessageRecordWebsocketHandler messageRecordWebsocketHandler;
    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 管理端-服务器监控
        registry.addHandler(messageRecordWebsocketHandler, "/ws/system/messageRecord")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOrigins("*");
        log.debug("WebSocket配置完成 - 路径: /ws/system/messageRecord");
    }
}
