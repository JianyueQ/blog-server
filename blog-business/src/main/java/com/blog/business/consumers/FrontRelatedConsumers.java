package com.blog.business.consumers;

import com.alibaba.fastjson2.JSON;
import com.blog.business.annotation.SendMessage;
import com.blog.business.constant.BusinessRabbitMqConstant;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.enums.MessageRecordType;
import com.blog.business.mapper.GuestbookMapper;
import com.blog.common.utils.spring.SpringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 前台相关消费者
 * @author 31373
 */
@Component
public class FrontRelatedConsumers {

    /**
     * 插入留言信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(BusinessRabbitMqConstant.GUESTBOOK_MESSAGE_QUEUE),
            exchange = @Exchange(BusinessRabbitMqConstant.GUESTBOOK_MESSAGE_EXCHANGE),
            key = BusinessRabbitMqConstant.GUESTBOOK_MESSAGE_KEY
    ))
    @SendMessage(messageTitle = "留言回复",
            messageContent = "您有一条留言回复需要查看!",
            messageType = MessageRecordType.GUESTBOOK)
    public void addGuestbookMessageRequest(String str) {
        Guestbook guestbook = JSON.parseObject(str, Guestbook.class);
        SpringUtils.getBean(GuestbookMapper.class).addMessage(guestbook);
    }

}
