package com.blog.business.rabbitmq;

import com.alibaba.fastjson2.JSON;
import com.blog.business.constant.BusinessRabbitMqConstant;
import com.blog.business.constant.FriendLinksConstant;
import com.blog.business.domain.dto.FriendLinksDto;
import com.blog.business.domain.entity.FriendLinks;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.ip.IpUtils;
import com.blog.common.utils.uuid.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

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

    public int sendFriendLinksRequest(FriendLinksDto friendLinksDto) {
        FriendLinks friendLinks = new FriendLinks();
        friendLinks.setName(friendLinksDto.getName());
        friendLinks.setUrl(friendLinksDto.getUrl());
        friendLinks.setLogo(friendLinksDto.getLogo());
        friendLinks.setDescription(friendLinksDto.getDescription());
        friendLinks.setEmail(friendLinksDto.getEmail());
        friendLinks.setStatus(FriendLinksConstant.PENDING);
        friendLinks.setCreateBy(IpUtils.getIpAddr());
        String json = JSON.toJSONString(friendLinks);
        rabbitTemplate.convertAndSend(BusinessRabbitMqConstant.FRIEND_LINKS_EXCHANGE, BusinessRabbitMqConstant.FRIEND_LINKS_KEY, json);
        return 1;
    }
}
