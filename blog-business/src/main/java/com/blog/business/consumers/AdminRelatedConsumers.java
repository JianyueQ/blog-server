package com.blog.business.consumers;

import com.alibaba.fastjson2.JSON;
import com.blog.business.constant.BusinessRabbitMqConstant;
import com.blog.business.domain.entity.FriendLinks;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.service.FriendLinksService;
import com.blog.business.service.VisitorRecordService;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 管理员相关消费者
 *
 * @author 31373
 */
@Component
public class AdminRelatedConsumers {

    private static final Logger log = LoggerFactory.getLogger(AdminRelatedConsumers.class);

    /**
     * 访客记录信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitMqConstants.VISITOR_QUEUE),
            exchange = @Exchange(RabbitMqConstants.VISITOR_EXCHANGE),
            key = RabbitMqConstants.VISITOR_KEY
    ))
    public void visitorRecord(String str) {
        VisitorRecord visitorRecord = JSON.parseObject(str, VisitorRecord.class);
        SpringUtils.getBean(VisitorRecordService.class).insertVisitorRecord(visitorRecord);
    }

    /**
     * 插入友链申请信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(BusinessRabbitMqConstant.FRIEND_LINKS_QUEUE),
            exchange = @Exchange(BusinessRabbitMqConstant.FRIEND_LINKS_EXCHANGE),
            key = BusinessRabbitMqConstant.FRIEND_LINKS_KEY
    ))
    public void friendLinksRequest(String str) {
        FriendLinks friendLinks = JSON.parseObject(str, FriendLinks.class);
        SpringUtils.getBean(FriendLinksService.class).insertFriendLinksRequest(friendLinks);
    }
}
