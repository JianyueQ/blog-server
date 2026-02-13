package com.blog.business.rabbitmq;

import com.alibaba.fastjson2.JSON;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.core.domain.model.LoginLog;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.ip.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ 异步任务管理
 * @author 31373
 */
@Component(value = "businessRabbitManager")
public class RabbitManager {

    private static final Logger log = LoggerFactory.getLogger(RabbitManager.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitManager(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendVisitorRecord(VisitorRecord visitorRecord) {
        String json = JSON.toJSONString(visitorRecord);
        rabbitTemplate.convertAndSend(RabbitMqConstants.VISITOR_EXCHANGE, RabbitMqConstants.VISITOR_KEY, json);
    }
}
