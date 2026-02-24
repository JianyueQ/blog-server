package com.blog.business.consumers;

import com.alibaba.fastjson2.JSON;
import com.blog.business.constant.BusinessRabbitMqConstant;
import com.blog.business.domain.entity.MessageRecord;
import com.blog.business.service.MessageRecordService;
import com.blog.business.websocket.MessageRecordWebsocketHandler;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.spring.SpringUtils;
import com.blog.system.domain.vo.AdministratorsVO;
import com.blog.system.mapper.SysUserMapper;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 消息相关消费者
 *
 * @author 31373
 */
@Component
public class MessageRelatedConsumers {

    /**
     * 是否已读 0-未读
     */
    private static final Integer IS_READ_N = 0;

    @Autowired
    private MessageRecordWebsocketHandler messageRecordWebsocketHandler;

    /**
     * 接收消息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(BusinessRabbitMqConstant.MESSAGE_RECORD_QUEUE),
            exchange = @Exchange(BusinessRabbitMqConstant.MESSAGE_RECORD_EXCHANGE),
            key = BusinessRabbitMqConstant.MESSAGE_RECORD_KEY
    ))
    public void receiveMessage(String str) throws Exception {
        MessageRecord messageRecord = JSON.parseObject(str, MessageRecord.class);
        if (StringUtils.isNotNull(messageRecord)) {
            insertMessageRecord(messageRecord);
        }
    }

    /**
     * 向信息记录表插入消息
     */
    private void insertMessageRecord(MessageRecord messageRecord) {
        if (StringUtils.isNull(messageRecord.getAdminId())) {
            // 如果管理员ID为空，则为广播,需要给全部管理员发送消息
            List<AdministratorsVO> administratorsVOS = SpringUtils.getBean(SysUserMapper.class).selectAdminList();
            for (AdministratorsVO administratorsVO : administratorsVOS) {
                messageRecord.setAdminId(administratorsVO.getAdminId());
                messageRecord.setIsRead(IS_READ_N);
                Date nowDate = DateUtils.getNowDate();
                messageRecord.setCreateTime(nowDate);
                SpringUtils.getBean(MessageRecordService.class).insertMessageRecord(messageRecord);
                messageRecordWebsocketHandler.sendMessageToOnlineAdmins(messageRecord);
            }
        } else {
            messageRecord.setIsRead(IS_READ_N);
            Date nowDate = DateUtils.getNowDate();
            messageRecord.setCreateTime(nowDate);
            SpringUtils.getBean(MessageRecordService.class).insertMessageRecord(messageRecord);
            messageRecordWebsocketHandler.sendMessageToOnlineAdmins(messageRecord);
        }
    }


}
