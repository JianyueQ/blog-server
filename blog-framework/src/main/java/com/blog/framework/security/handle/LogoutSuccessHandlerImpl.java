package com.blog.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.MessageUtils;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.StringUtils;
import com.blog.framework.web.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 登出成功处理类
 *
 * @author 31373
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(LogoutSuccessHandlerImpl.class);
    private final TokenService tokenService;

    private final RabbitTemplate rabbitTemplate;

    private final RedisCache redisCache;

    public LogoutSuccessHandlerImpl(TokenService tokenService, RabbitTemplate rabbitTemplate, RedisCache redisCache) {
        this.tokenService = tokenService;
        this.rabbitTemplate = rabbitTemplate;
        this.redisCache = redisCache;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUserOnAdmin loginUserOnAdmin = tokenService.getLoginUserOnAdmin(request);
        if (StringUtils.isNotNull(loginUserOnAdmin)) {
            String token = loginUserOnAdmin.getToken();
            Map<String, Object> updateUserInfoForAdmin = new HashMap<>();
            updateUserInfoForAdmin.put("adminId", loginUserOnAdmin.getAdminId());
            updateUserInfoForAdmin.put("loginTime", DateUtils.getTime());
            updateUserInfoForAdmin.put("ipaddr", loginUserOnAdmin.getIpaddr());
            //todo 更新登录时间和登录的ip
            try {
                rabbitTemplate.convertAndSend("admin.details.exchange", "admin.logout", updateUserInfoForAdmin);
            } catch (AmqpException e) {
                log.error("RabbitMQ发送消息异常:{}", e.getMessage());
            }
            // 删除用户缓存记录
            tokenService.delLoginUserOnAdmin(token);
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }


}
