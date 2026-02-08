package com.blog.framework.rabbitmq;

import com.alibaba.fastjson2.JSON;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.core.domain.model.LoginLog;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.ip.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 异步任务管理
 * @author 31373
 */
@Component
public class RabbitManager {

    private static final Logger log = LoggerFactory.getLogger(RabbitManager.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitManager(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     *记录登录信息
     */
    public void recordLoginInfo(Authentication authentication){
        try {
            LoginUserOnAdmin loginUserOnAdmin = (LoginUserOnAdmin) authentication.getPrincipal();
            String ip = IpUtils.getIpAddr();
            //记录登录信息
            Map<String, Object> updateUserInfoForAdmin = new HashMap<>();
            updateUserInfoForAdmin.put("adminId", loginUserOnAdmin.getAdminId());
            updateUserInfoForAdmin.put("loginTime", DateUtils.getTime());
            updateUserInfoForAdmin.put("ipaddr", ip);
            rabbitTemplate.convertAndSend(RabbitMqConstants.ADMIN_DETAIL_EXCHANGE, RabbitMqConstants.ADMIN_LOGIN_KEY, updateUserInfoForAdmin);
            log.debug("登录信息消息已发送至队列,ip={}",ip);
        } catch (Exception e) {
            log.error("发送登录信息消息失败: ", e);
        }
    }

    /**
     * 记录登录信息
     */
    public void recordLogininfor(final String username, final String status, final String message, final Object... args){
        try {
            final String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
            final String ip = IpUtils.getIpAddr();
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setStatus(status);
            loginLog.setMessage(message);
            loginLog.setArgs(args);
            loginLog.setIp(ip);
            loginLog.setUserAgent(userAgent);
            String jsonString = JSON.toJSONString(loginLog);
            rabbitTemplate.convertAndSend(RabbitMqConstants.ADMIN_DETAIL_EXCHANGE,RabbitMqConstants.ADMIN_LOGIN_LOG_KEY,jsonString);
            log.debug("登录日志消息已发送至队列: username={}, status={}", username, status);
        } catch (Exception e) {
            log.error("发送登录日志消息失败: username={}, status={}", username, status, e);
        }
    }

}
