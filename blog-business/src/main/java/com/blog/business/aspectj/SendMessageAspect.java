package com.blog.business.aspectj;

import com.blog.business.annotation.SendMessage;
import com.blog.business.domain.entity.MessageRecord;
import com.blog.business.rabbitmq.RabbitManager;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.utils.spring.SpringUtils;
import com.blog.system.mapper.SysUserMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 31373
 */
@Aspect
@Component
public class SendMessageAspect {

    private static final Logger log = LoggerFactory.getLogger(SendMessageAspect.class);

    @Autowired
    private RabbitManager rabbitManager;

    /**
     * 处理请求后执行
     *
     * @param joinPoint   切点
     * @param sendMessage 自定义注解
     */
    @AfterReturning(value = "@annotation(sendMessage)", returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result, SendMessage sendMessage) {
        handleSendMessage(joinPoint, result, sendMessage);
    }

    /**
     * 处理发送消息
     *
     * @param joinPoint   切点
     * @param sendMessage 自定义注解
     */
    protected void handleSendMessage(JoinPoint joinPoint, Object result, SendMessage sendMessage) {
        try {
            if (sendMessage.broadcast()) {
                if (sendMessage.receiveUsernames().length == 0) {
                    // 广播模式：给所有管理员发送消息
                    handleBroadcastMessage(sendMessage);
                } else {
                    throw new RuntimeException("指定用户模式下，receiveUsernames参数不能为空");
                }
            } else {
                // 指定用户模式：给指定的管理员发送消息
                handleSpecificUsersMessage(sendMessage);
            }
        } catch (Exception e) {
            log.error("处理消息发送失败", e);
        }
    }






    /**
     * 处理指定用户模式的消息发送
     *
     * @param sendMessage 自定义注解
     */
    private void handleSpecificUsersMessage(SendMessage sendMessage) {
        String[] usernames = sendMessage.receiveUsernames();
        for (String username : usernames) {
            Administrators administrators = SpringUtils.getBean(SysUserMapper.class).selectUserByUserName(username);
            MessageRecord messageRecord = new MessageRecord();
            messageRecord.setMessageTitle(sendMessage.messageTitle());
            messageRecord.setMessageContent(sendMessage.messageContent());
            messageRecord.setMessageType(sendMessage.messageType().name());
            messageRecord.setAdminId(administrators.getAdminId());
            // 异步处理：发送到RabbitMQ
            rabbitManager.sendMessageRecord(messageRecord);
        }
    }

    /**
     * 处理广播模式的消息发送
     *
     * @param sendMessage 自定义注解
     */
    private void handleBroadcastMessage(SendMessage sendMessage) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setMessageTitle(sendMessage.messageTitle());
        messageRecord.setMessageContent(sendMessage.messageContent());
        messageRecord.setMessageType(sendMessage.messageType().name());

        // 异步处理：发送到RabbitMQ
        rabbitManager.sendMessageRecord(messageRecord);
    }
}