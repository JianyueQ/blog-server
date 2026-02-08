package com.blog.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.blog.common.constant.Constants;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.MessageUtils;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.StringUtils;
import com.blog.framework.rabbitmq.RabbitManager;
import com.blog.framework.web.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
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

    private final RabbitManager rabbitManager;

    public LogoutSuccessHandlerImpl(TokenService tokenService, RabbitTemplate rabbitTemplate, RabbitManager rabbitManager) {
        this.tokenService = tokenService;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitManager = rabbitManager;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUserOnAdmin loginUserOnAdmin = tokenService.getLoginUserOnAdmin(request);
        if (StringUtils.isNotNull(loginUserOnAdmin)) {
            String token = loginUserOnAdmin.getToken();
            String username = loginUserOnAdmin.getUsername();
            String message = MessageUtils.message("user.logout.success");
            String logout = Constants.LOGOUT;
            // 记录用户退出日志
            rabbitManager.recordLogininfor(username, logout, message);
            // 删除用户缓存记录
            tokenService.delLoginUserOnAdmin(token);
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }


}
