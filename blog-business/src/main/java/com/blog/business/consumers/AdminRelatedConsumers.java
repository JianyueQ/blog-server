package com.blog.business.consumers;

import com.alibaba.fastjson2.JSON;
import com.blog.business.annotation.SendMessage;
import com.blog.business.constant.BusinessRabbitMqConstant;
import com.blog.business.domain.entity.FriendLinks;
import com.blog.business.domain.entity.VisitorInfo;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.domain.entity.VisitorRecordParameters;
import com.blog.business.enums.MessageRecordType;
import com.blog.business.mapper.VisitorRecordMapper;
import com.blog.business.service.FriendLinksService;
import com.blog.business.utils.FingerprintUtils;
import com.blog.common.constant.RabbitMqConstants;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.spring.SpringUtils;
import com.blog.common.utils.uuid.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    @SendMessage(messageTitle = "访客记录", messageContent = "有访客来访!", messageType = MessageRecordType.VISITORS)
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(RabbitMqConstants.VISITOR_QUEUE),
            exchange = @Exchange(RabbitMqConstants.VISITOR_EXCHANGE),
            key = RabbitMqConstants.VISITOR_KEY
    ))
    public void visitorRecord(String str) {
        VisitorRecord visitorRecord = JSON.parseObject(str, VisitorRecord.class);
        VisitorRecordMapper visitorRecordMapper = SpringUtils.getBean(VisitorRecordMapper.class);
        String decrypt = FingerprintUtils.decrypt(visitorRecord.getClientData());
        VisitorRecordParameters visitorRecordParameters = JSON.parseObject(decrypt, VisitorRecordParameters.class);
        VisitorInfo visitorInfo = new VisitorInfo();
        if (StringUtils.isNotNull(visitorRecordParameters)) {
            visitorInfo.setAvatar(visitorRecordParameters.getVisitor().getAvatar());
            visitorInfo.setEmail(visitorRecordParameters.getVisitor().getEmail());
            visitorInfo.setNickname(visitorRecordParameters.getVisitor().getNickname());
        }
        //构造访客指纹,用于唯一标识访客
        String fingerprint = generateVisitorFingerprint(visitorRecord.getUserAgent(), visitorRecordParameters);
        visitorRecord.setFingerprint(fingerprint);
        //根据构造出的访客指纹查询数据库中是否存在该访客
        VisitorRecord visitorRecordInDB = visitorRecordMapper.getVisitorRecordByFingerprint(fingerprint);
        if (StringUtils.isNotNull(visitorRecordInDB)) {
            visitorRecordInDB.setFingerprint(fingerprint);
            //修改访问次数
            visitorRecordMapper.updateVisitorRecord(visitorRecordInDB);
            //修改访客信息
            visitorInfo.setVisitorInfoId(visitorRecordInDB.getVisitorInfoId());
            visitorInfo.setUpdateTime(DateUtils.getNowDate());
            visitorRecordMapper.updateVisitorInfo(visitorInfo);
        } else {
            visitorInfo.setCreateTime(DateUtils.getNowDate());
            visitorInfo.setCreateBy(visitorRecord.getIpaddr());
            visitorRecordMapper.insertVisitorInfo(visitorInfo);
            visitorRecord.setVisitorInfoId(visitorInfo.getVisitorInfoId());
            visitorRecordMapper.insertVisitorRecord(visitorRecord);
        }
    }

    /**
     * 生成访客指纹并记录访客信息
     *
     * @return 指纹字符串
     */
    private String generateVisitorFingerprint(String userAgent, VisitorRecordParameters clientData) {
        try {
            // 收集用于生成指纹的信息
            StringBuilder fingerprintBuilder = new StringBuilder();
            if (StringUtils.isNotNull(clientData)) {
                //删除实体中的昵称,头像,邮箱
                clientData.setVisitor(null);
                String jsonString = JSON.toJSONString(clientData);
                fingerprintBuilder.append(jsonString);
            }
            // 用户代理字符串（浏览器特征）
            if (StringUtils.isNotEmpty(userAgent)) {
                fingerprintBuilder.append(userAgent);
            }
            // 生成MD5哈希作为指纹
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(fingerprintBuilder.toString().getBytes(StandardCharsets.UTF_8));

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("生成访客指纹时发生错误", e);
            // 备用方案：使用UUID
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    /**
     * 插入友链申请信息
     */
    @SendMessage(messageTitle = "友链申请", messageContent = "您有一条友链申请需要审核!", messageType = MessageRecordType.FRIEND_LINKS)
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
