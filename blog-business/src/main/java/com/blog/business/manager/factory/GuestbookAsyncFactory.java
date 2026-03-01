package com.blog.business.manager.factory;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.constant.GuestbookConstants;
import com.blog.business.domain.dto.FrontGuestbookListDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.domain.vo.GuestbookListVo;
import com.blog.business.mapper.GuestbookMapper;
import com.blog.business.service.GuestbookService;
import com.blog.business.utils.HotScoreUtils;
import com.blog.common.constant.Constants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
public class GuestbookAsyncFactory {

    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 删除单个根留言及子留言
     *
     * @param guestbookList 留言列表
     * @return
     */
    public static TimerTask deleteGuestbookMessage(final List<Guestbook> guestbookList) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
                    String frontCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY;
                    //删除缓存中的根留言
                    String frontRootIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + GuestbookConstants.ROOT_ID;
                    String frontRootListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_KEY;
                    String frontRootCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_COUNT_KEY;
                    //删除缓存中的子留言
                    String frontChildListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
                    String frontChildCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
                    // frontCount计数器
                    long frontCount = 0;
                    for (Guestbook guestbook : guestbookList) {
                        if (guestbook.getIsRoot().equals(GuestbookConstants.IS_ROOT)) {
                            //删除根留言索引
                            redisCache.deleteCacheZSetValue(frontRootIndexCacheKey, guestbook.getGuestbookId().toString());
                            //删除根留言数据
                            redisCache.deleteCacheMapValue(frontRootListCacheKey, guestbook.getGuestbookId().toString());
                            //减少根留言总数
                            redisCache.decrement(frontRootCountCacheKey);
                            //删除根留言的子留言的总数
                            redisCache.deleteCacheMapValue(frontChildCountCacheKey, guestbook.getGuestbookId().toString());
                        } else {
                            String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + guestbook.getRootId();
                            //删除子留言索引
                            redisCache.deleteCacheZSetValue(frontChildIndexCacheKey, guestbook.getGuestbookId().toString());
                            //删除子留言数据
                            redisCache.deleteCacheMapValue(frontChildListCacheKey, guestbook.getGuestbookId().toString());
                            //减少根留言的子留言总数
                            redisCache.hashDecrementIfPresent(frontChildCountCacheKey, guestbook.getRootId().toString());
                        }
                        //计数器++
                        frontCount++;
                    }
                    //更新留言总数
                    redisCache.decrement(frontCountCacheKey, frontCount);
                } catch (BeansException e) {
                    sys_user_logger.error("删除单个根留言及子留言出错:{}", e.getMessage());
                }
            }
        };
    }

    /**
     * 处理留言数据到redis中
     *
     * @param frontGuestbookList 留言列表
     * @param total              留言总数
     * @param
     */
    public static TimerTask handleGuestbookListToRedis(final List<Guestbook> frontGuestbookList, final Integer guestbookAllCount, final Integer total) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
                    GuestbookService guestbookService = SpringUtils.getBean(GuestbookService.class);
                    for (Guestbook guestbookList : frontGuestbookList) {
                        //提取出根评论id,将id存入redis 有序集合作为索引 value 为根评论id,分数为热度分数
                        String messageTime = guestbookList.getMessageTime();
                        Integer replyCount = guestbookList.getReplyCount();
                        String guestbookId = guestbookList.getGuestbookId().toString();
                        Long rootId = guestbookList.getRootId();
                        Date messageTimeDate = DateUtils.parseDate(messageTime);
                        double hotScore = HotScoreUtils.calculateHotScore(messageTimeDate, replyCount);
                        String frontRootIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + rootId;
                        String frontRootListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_KEY;
                        String frontCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY;
                        String frontChildCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
                        String frontRootCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_COUNT_KEY;
                        //将留言信息索引存储到Redis中,索引过期时间设置为一天
                        redisCache.setCacheZSetValue(frontRootIndexCacheKey, guestbookId, hotScore, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.DAYS);
                        //将总评论数缓存到redis中,设置两天过期
                        if (guestbookAllCount > 0) {
                            redisCache.setCacheObject(frontCountCacheKey, guestbookAllCount, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                        } else {
                            redisCache.setCacheObject(frontCountCacheKey, 0, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                        }
                        //将根评论数缓存到redis中,,数据过期的时间设置为两天
                        if (total > 0) {
                            redisCache.setCacheObject(frontRootCountCacheKey, total, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                        } else {
                            redisCache.setCacheObject(frontRootCountCacheKey, 0, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                        }
                        //将根评论信息存储到Redis中,设置过期时间为2天
                        redisCache.setCacheMapValue(frontRootListCacheKey, guestbookId, guestbookList);
                        redisCache.expire(frontRootListCacheKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                        //将回复数缓存到redis中,设置过期时间为2天
                        if (replyCount > 0) {
                            redisCache.setCacheMapValue(frontChildCountCacheKey, guestbookId, replyCount);
                        } else {
                            redisCache.setCacheMapValue(frontChildCountCacheKey, guestbookId, 0);
                        }
                        redisCache.expire(frontChildCountCacheKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                        //预热子评论相关数据
                        FrontGuestbookListDto guestbookListDto = new FrontGuestbookListDto();
                        guestbookListDto.setIsRoot(guestbookList.getIsRoot());
                        guestbookListDto.setGuestbookId(guestbookList.getGuestbookId());
                        guestbookService.cacheAllFrontChildGuestbookList(guestbookListDto);
                    }
                } catch (Exception e) {
                    sys_user_logger.error("前台保存留言信息到Redis缓存出错:", e);
                }
            }
        };
    }

    /**
     * 处理子留言数据到redis中
     * @param childGuestbookList 子留言列表
     * @param totalInt 子留言总数
     * @return TimerTask
     */
    public static TimerTask handleGuestbookChildListToRedis(List<Guestbook> childGuestbookList, Integer totalInt) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
                    for (Guestbook guestbook : childGuestbookList) {
                        Long guestbookId = guestbook.getGuestbookId();
                        Long rootId = guestbook.getRootId();
                        String frontChildCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
                        String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + guestbook.getRootId();
                        String frontCacheGuestbookChildListKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
                        if(totalInt > 0){
                            redisCache.setCacheMapValue(frontChildCountCacheKey,rootId.toString(),totalInt);
                        }else {
                            redisCache.setCacheMapValue(frontChildCountCacheKey,rootId.toString(),0);
                        }
                        redisCache.expire(frontChildCountCacheKey, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
                        //将子留言信息索引存储到Redis中,设置过期时间为1天
                        redisCache.setCacheZSetValue(frontChildIndexCacheKey, guestbookId.toString(), guestbook.getTimestamp(), BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.DAYS);
                        //将留言信息存储到Redis中,设置过期时间为2天
                        redisCache.setCacheMapValue(frontCacheGuestbookChildListKey, guestbookId.toString(), guestbook);
                        redisCache.expire(frontCacheGuestbookChildListKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
                    }
                } catch (Exception e) {
                    sys_user_logger.error("前台保存子留言信息到Redis缓存出错:", e);
                }
            }
        };
    }

    /**
     * 处理子留言数据到redis中
     * @param guestbookListDto 留言列表dto
     * @return TimerTask
     */
    public static TimerTask handleGuestbookChildListToRedis(FrontGuestbookListDto guestbookListDto) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SpringUtils.getBean(GuestbookService.class).cacheAllFrontChildGuestbookList(guestbookListDto);
                } catch (Exception e) {
                    sys_user_logger.error("前台保存子留言信息到Redis缓存出错:", e);
                }
            }
        };
    }

    public static TimerTask saveGuestbookListToRedis(final List<GuestbookListVo> guestbookListVos, final long total, final String baseCacheKey, final String countCacheKey) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
                    GuestbookService guestbookService = SpringUtils.getBean(GuestbookService.class);
                    for (GuestbookListVo guestbookListVo : guestbookListVos) {
                        String messageTime = guestbookListVo.getMessageTime();
                        //将留言时间转换成时间戳
                        long timestamp = Objects.requireNonNull(DateUtils.parseDate(messageTime)).getTime() != 0 ? DateUtils.parseDate(messageTime).getTime() : 0;
                        //将留言信息存储到Redis中,设置过期时间为1天
                        redisCache.setCacheZSetValue(baseCacheKey, guestbookListVo, timestamp, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
                        if (total > -1) {
                            //将总评论数缓存到redis中
                            redisCache.setCacheObject(countCacheKey, total, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
                        }
                        //预热子评论缓存
                        Integer isRoot = guestbookListVo.getIsRoot();
                        if (GuestbookConstants.IS_ROOT.equals(isRoot)) {
                            GuestbookListDto guestbookListDto = new GuestbookListDto();
                            guestbookListDto.setGuestbookId(guestbookListVo.getGuestbookId());
                            guestbookService.cacheAllChildGuestbookList(guestbookListDto);
                        }
                    }
                } catch (BeansException e) {
                    sys_user_logger.error("保存留言信息到Redis缓存出错:{}", e.getMessage());
                }
            }
        };
    }

    public static TimerTask updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto) {
        return new TimerTask() {
            @Override
            public void run() {
                Long guestbookId = guestbookStatusDto.getGuestbookId();
                RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
                Guestbook guestbook = SpringUtils.getBean(GuestbookMapper.class).getGuestbookMessageById(guestbookId);
                Long rootId = guestbook.getRootId();
                Integer status = guestbook.getStatus();
                String frontCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY;
                // 如果是根留言
                if (GuestbookConstants.IS_ROOT.equals(guestbook.getIsRoot())) {
                    String frontRootIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + rootId;
                    String frontRootListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_KEY;
                    String frontRootCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_COUNT_KEY;
                    //判断状态
                    if (GuestbookConstants.STATUS_SHOW.equals(status)) {
                        double hotScore = HotScoreUtils.calculateHotScore(guestbook.getCreateTime(), guestbook.getReplyCount());
                        //显示状态,则插入根留言索引
                        redisCache.setCacheZSetValue(frontRootIndexCacheKey, guestbookId.toString(), hotScore);
                        //增加留言总数量
                        redisCache.increment(frontCountCacheKey);
                        //增加根留言总数量
                        redisCache.increment(frontRootCountCacheKey);
                    } else if (GuestbookConstants.STATUS_AUDITING.equals(status)) {
                        //审核状态,则删除根留言索引
                        redisCache.deleteCacheZSetValue(frontRootIndexCacheKey, guestbookId.toString());
                        //减少留言总数量
                        redisCache.decrement(frontCountCacheKey);
                        //减少根留言总数量
                        redisCache.decrement(frontRootCountCacheKey);
                    } else if (GuestbookConstants.STATUS_HIDDEN.equals(status)) {
                        //隐藏状态,则删除根留言索引
                        redisCache.deleteCacheZSetValue(frontRootIndexCacheKey, guestbookId.toString());
                        //减少留言总数量
                        redisCache.decrement(frontCountCacheKey);
                        //减少根留言总数量
                        redisCache.decrement(frontRootCountCacheKey);
                    }
                } else {
                    String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + rootId;
                    String frontCacheGuestbookChildListKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
                    String frontChildCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
                    //判断状态
                    if (GuestbookConstants.STATUS_SHOW.equals(status)) {
                        double hotScore = HotScoreUtils.calculateHotScore(guestbook.getCreateTime(), guestbook.getReplyCount());
                        //显示状态,则插入子留言索引
                        redisCache.setCacheZSetValue(frontChildIndexCacheKey, guestbookId.toString(), hotScore);
                        //增加留言总数量
                        redisCache.increment(frontCountCacheKey);
                        //增加根留言的子留言总数量
                        redisCache.hashIncrement(frontChildCountCacheKey, rootId.toString());
                    } else if (GuestbookConstants.STATUS_AUDITING.equals(status)) {
                        //审核状态,则删除子留言索引
                        redisCache.deleteCacheZSetValue(frontChildIndexCacheKey, guestbookId.toString());
                        //减少留言总数量
                        redisCache.decrement(frontCountCacheKey);
                        //减少子留言总数量
                        redisCache.hashDecrement(frontChildCountCacheKey, rootId.toString());
                    } else if (GuestbookConstants.STATUS_HIDDEN.equals(status)) {
                        //隐藏状态,则删除子留言索引
                        redisCache.deleteCacheZSetValue(frontChildIndexCacheKey, guestbookId.toString());
                        //减少留言总数量
                        redisCache.decrement(frontCountCacheKey);
                        //减少子留言总数量
                        redisCache.hashDecrement(frontChildCountCacheKey, rootId.toString());
                    }
                }
            }
        };
    }



}
