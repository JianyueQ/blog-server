package com.blog.business.consumers;

import com.alibaba.fastjson2.JSON;
import com.blog.business.annotation.SendMessage;
import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.constant.BusinessRabbitMqConstant;
import com.blog.business.constant.GuestbookConstants;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.enums.MessageRecordType;
import com.blog.business.mapper.GuestbookMapper;
import com.blog.business.utils.HotScoreUtils;
import com.blog.common.constant.Constants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 留言相关消费者
 * @author 31373
 */
@Component
public class GuestbookRelatedConsumers {

    private static final Logger log = LoggerFactory.getLogger(GuestbookRelatedConsumers.class);

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
        try {
            RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
            Guestbook guestbook = JSON.parseObject(str, Guestbook.class);
            SpringUtils.getBean(GuestbookMapper.class).addMessage(guestbook);
            if (!guestbook.getIsRoot().equals(GuestbookConstants.IS_ROOT)) {
                //不是根留言则获取rootId 0 为根留言 和 回复留言id 0 为直接回复根留言
                Long rootId = guestbook.getRootId();
//                Long parentId = guestbook.getParentId();
                //根据rootId更新根评论的回复数量
                SpringUtils.getBean(GuestbookMapper.class).updateReplyCount(rootId);
                //更新缓存的根评论的回复数量 和 更新总的评论数量
                String frontChildCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
                redisCache.hashIncrement(frontChildCacheKey, rootId.toString());
                redisCache.expire(frontChildCacheKey, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
                String frontListCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY;
                redisCache.incrementIfPresent(frontListCountCacheKey);
                redisCache.expire(frontListCountCacheKey, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
                //根据rootId获取根评论的信息进行更新根评论的热度
                Guestbook rootGuestbook = SpringUtils.getBean(GuestbookMapper.class).getGuestbookMessageById(rootId);
                String guestbookId = rootGuestbook.getGuestbookId().toString();
                //更新根评论的热度
                double score = HotScoreUtils.calculateHotScore(rootGuestbook.getCreateTime(), rootGuestbook.getReplyCount());
                //按照热度排序根评论的索引
                redisCache.setCacheZSetValue(BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + GuestbookConstants.ROOT_ID, guestbookId, score, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
            } else {
                //是根评论则更新总评论数量
                redisCache.increment(BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
                //更新根评论数量
                redisCache.increment(BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_COUNT_KEY, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
            }
        } catch (Exception e) {
            log.error("插入留言信息发生了错误:", e);
        }
    }

}
