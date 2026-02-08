package com.blog.framework.websocket.interceptor;

import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.Constants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.StringUtils;
import com.blog.framework.web.service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;


import java.util.Map;

/**
 * WebSocket握手拦截器
 * @author 31373
 */
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);
    /**
     * URL参数 - token
     */
    public static final String TOKEN_PARAM = "token";
    /**
     * WebSocket连接属性 - uuid
     */
    public static final String UUID = "uuid";
    /**
     * WebSocket连接属性 - 当前用户
     */
    public static final String CURRENT_USER = "currentUser";
    /**
     * WebSocket连接属性 - 管理员ID
     */
    public static final String ADMIN_ID = "adminId";
    /**
     * WebSocket连接属性 - 连接时间
     */
    public static final String CONNECTION_TIME = "connectionTime";
    /**
     * WebSocket连接属性 - 最后心跳时间
     */
    public static final String LAST_HEARTBEAT = "lastHeartbeat";

    private final TokenService tokenService;
    private final RedisCache redisCache;

    public AuthHandshakeInterceptor(TokenService tokenService, RedisCache redisCache) {
        this.tokenService = tokenService;
        this.redisCache = redisCache;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

            // 从URL参数获取token
            String token = servletRequest.getParameter(TOKEN_PARAM);

            if (StringUtils.isEmpty(token)) {
                log.warn("WebSocket连接缺少认证token");
                return false;
            }
            if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
                token = token.replace(Constants.TOKEN_PREFIX, "");
            }
            try {
                // 验证token并获取用户信息
                Claims claims = tokenService.parseToken(token);
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                if (StringUtils.isNotEmpty(uuid)){
                    String userKey = tokenService.getTokenKey(uuid);
                    LoginUserOnAdmin user = redisCache.getCacheObject(userKey);
                    if (StringUtils.isNotNull(user)) {
                        String username = user.getUsername();
                        Long adminId = user.getAdminId();
                        attributes.put(CURRENT_USER, username);
                        attributes.put(UUID, uuid);
                        attributes.put(ADMIN_ID, adminId);
                        attributes.put(CONNECTION_TIME,System.currentTimeMillis());
                        attributes.put(LAST_HEARTBEAT,System.currentTimeMillis());
                        log.info("WebSocket认证成功 - 用户: {},ID: {}", username, adminId);
                        return true;
                    }
                }else{
                    log.warn("WebSocket认证失败 - Redis中未找到用户信息, UUID: {}", uuid);
                }
            } catch (Exception e) {
                log.error("WebSocket token验证失败: {}", e.getMessage());
            }
        }

        log.warn("WebSocket认证失败");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 连接建立后的处理
        log.debug("WebSocket握手完成");
    }

}