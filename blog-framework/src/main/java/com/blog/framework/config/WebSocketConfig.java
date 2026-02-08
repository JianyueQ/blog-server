package com.blog.framework.config;

import com.blog.framework.websocket.handler.impl.AdminServerMonitorWebSocketHandler;
import com.blog.framework.websocket.interceptor.AuthHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * @author 31373
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    private final AuthHandshakeInterceptor authHandshakeInterceptor;

    public WebSocketConfig(AuthHandshakeInterceptor authHandshakeInterceptor) {
        this.authHandshakeInterceptor = authHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 管理端-服务器监控
        registry.addHandler(adminServerMonitorWebSocketHandler(), "/ws/monitor/server")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOrigins("*");
        log.info("WebSocket配置完成 - 路径: /ws/monitor/server");
    }

    @Bean
    public AdminServerMonitorWebSocketHandler adminServerMonitorWebSocketHandler() {
        return new AdminServerMonitorWebSocketHandler();
    }


    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(1024 * 1024);
        container.setMaxTextMessageBufferSize(1024 * 1024);
        container.setAsyncSendTimeout(15000L);
        return container;
    }
}
