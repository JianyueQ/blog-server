package com.blog.business.service.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.constant.GuestbookConstants;
import com.blog.business.domain.dto.FrontGuestbookListDto;
import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.domain.vo.GuestbookListVo;
import com.blog.business.manager.factory.GuestbookAsyncFactory;
import com.blog.business.mapper.GuestbookMapper;
import com.blog.business.rabbitmq.RabbitManager;
import com.blog.business.service.GuestbookService;
import com.blog.business.utils.HotScoreUtils;
import com.blog.common.constant.Constants;
import com.blog.common.constant.HttpStatus;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.core.page.PageDomain;
import com.blog.common.core.page.TableSupport;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.SnowflakeUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import com.blog.common.utils.page.PageUtils;
import com.blog.common.utils.spring.SpringUtils;
import com.blog.framework.manager.AsyncManager;
import com.blog.system.service.ConfigService;
import com.blog.system.service.SysConfigService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Service
public class GuestbookServiceImpl implements GuestbookService {

    private static final Logger log = LoggerFactory.getLogger(GuestbookServiceImpl.class);

    private static final String CONFIG_KEY = "sys.comment.enabled";

    @Autowired
    private GuestbookMapper guestbookMapper;
    @Autowired
    private RabbitManager rabbitManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ConfigService configService;

    @Override
    public Guestbook addMessage(GuestbookDto guestbookDto) {
        Guestbook guestbook = new Guestbook();
        Long rootId = guestbookDto.getRootId();
        String ipAddr = IpUtils.getIpAddr();
        //生成雪花id
        long guestbookId = SnowflakeUtils.generateId();
        guestbook.setGuestbookId(guestbookId);
        guestbook.setNickname(guestbookDto.getNickname());
        guestbook.setEmail(guestbookDto.getEmail());
        guestbook.setContent(guestbookDto.getContent());
        guestbook.setAvatar(guestbookDto.getAvatar());
        guestbook.setRootId(Objects.requireNonNullElse(rootId, GuestbookConstants.ROOT_ID));
        if (GuestbookConstants.ROOT_ID.equals(rootId)) {
            guestbook.setIsRoot(GuestbookConstants.IS_ROOT);
        } else {
            guestbook.setIsRoot(GuestbookConstants.NOT_ROOT);
        }
        if (StringUtils.isNotNull(guestbookDto.getParentId())) {
            guestbook.setParentId(guestbookDto.getParentId());
        } else {
            guestbook.setParentId(GuestbookConstants.PARENT_ID);
        }
        guestbook.setTimestamp(System.currentTimeMillis());
        guestbook.setCreateTime(DateUtils.getNowDate());
        guestbook.setMessageTime(DateUtils.getTime());
        guestbook.setCreateBy(ipAddr);
        guestbook.setLocation(AddressUtils.getRealAddressByIP(ipAddr));
        //获取系统设置中,是否需要审核
        String keyValue = configService.selectConfigByKey(CONFIG_KEY);
        if (GuestbookConstants.COMMENT_SWITCH_OPEN.equals(keyValue)){
            guestbook.setStatus(GuestbookConstants.STATUS_AUDITING);
        } else if (GuestbookConstants.COMMENT_SWITCH_CLOSE.equals(keyValue)) {
            //更新redis数据 先加索引 在加hash类型的数据
            if (guestbook.getIsRoot().equals(GuestbookConstants.IS_ROOT)) {
                String frontRootIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + GuestbookConstants.ROOT_ID;
                String frontRootListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_KEY;
                String guestbookIdToString = guestbook.getGuestbookId().toString();
                //计算分数
                double score = HotScoreUtils.calculateHotScore(guestbook.getCreateTime(), 0);
                //判断索引是否存在
                if (!redisCache.hasKey(frontRootIndexCacheKey)) {
                    //如果索引不存在,则查询数据库并添加到索引中
                    selectFrontRootGuestbookList(new FrontGuestbookListDto(), false);
                }
                //按照热度排序根评论的索引,索引过期时间设置为一天
                redisCache.setCacheZSetValue(frontRootIndexCacheKey, guestbookIdToString, score, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.DAYS);
                //添加根评论数据,数据过期的时间设置为两天
                redisCache.setCacheMapValue(frontRootListCacheKey, guestbookIdToString, guestbook);
                redisCache.expire(frontRootListCacheKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
            } else {
                String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + guestbook.getRootId();
                String frontChildListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
                String guestbookIdToString = guestbook.getGuestbookId().toString();
                //判断索引是否存在
                if (!redisCache.hasKey(frontChildIndexCacheKey)) {
                    FrontGuestbookListDto frontGuestbookListDto = new FrontGuestbookListDto();
                    frontGuestbookListDto.setGuestbookId(rootId);
                    //如果索引不存在,则查询数据库并添加到索引中
                    selectFrontChildGuestbookList(frontGuestbookListDto, false);
                }
                //根据父评论id查询hash结构的redis中是否有该评论的信息,如果有则获取昵称并set到guestbook对象中
                Guestbook parentGuestbook = redisCache.getCacheMapValue(frontChildListCacheKey, guestbook.getParentId().toString());
                if (StringUtils.isNotNull(parentGuestbook)){
                    guestbook.setParentNickname(parentGuestbook.getNickname());
                }
                //按照时间戳排序子评论的索引,索引过期时间设置为一天
                redisCache.setCacheZSetValue(frontChildIndexCacheKey, guestbookIdToString, guestbook.getTimestamp(), BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.DAYS);
                //添加子评论数据,数据过期的时间设置为两天
                redisCache.setCacheMapValue(frontChildListCacheKey, guestbookIdToString, guestbook);
                redisCache.expire(frontChildListCacheKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
            }
        }
        rabbitManager.sendAddGuestbookMessageRequest(guestbook);
        return guestbook;
    }

    @Override
    public Guestbook adminReplyMessage(GuestbookDto guestbookDto) {
        Guestbook guestbook = new Guestbook();
        Administrators administrators = SecurityUtils.getLoginUserOnAdmin().getAdministrators();
        Long rootId = guestbookDto.getRootId();
        //生成雪花id
        long guestbookId = SnowflakeUtils.generateId();
        guestbook.setGuestbookId(guestbookId);
        guestbook.setNickname(administrators.getNickname());
        guestbook.setEmail(administrators.getEmail());
        guestbook.setAvatar(administrators.getAvatar());
        guestbook.setContent(guestbookDto.getContent());
        guestbook.setRootId(guestbookDto.getRootId());
        guestbook.setIsRoot(GuestbookConstants.NOT_ROOT);
        guestbook.setParentId(guestbookDto.getParentId());
        guestbook.setMessageTime(DateUtils.getTime());
        guestbook.setTimestamp(System.currentTimeMillis());
        guestbook.setCreateTime(DateUtils.getNowDate());
        guestbook.setCreateBy(String.valueOf(administrators.getAdminId()));
        guestbook.setLocation(AddressUtils.getRealAddressByIP(IpUtils.getIpAddr()));
        guestbookMapper.addMessage(guestbook);
        //更新redis数据 先加索引 在加hash类型的数据
        String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + guestbook.getRootId();
        String frontChildListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
        String guestbookIdToString = guestbook.getGuestbookId().toString();
        //根据父评论id查询hash结构的redis中是否有该评论的信息,如果有则获取昵称并set到guestbook对象中
        Guestbook parentGuestbook = redisCache.getCacheMapValue(frontChildListCacheKey, guestbook.getParentId().toString());
        if (StringUtils.isNotNull(parentGuestbook)){
            guestbook.setParentNickname(parentGuestbook.getNickname());
        }
        //按照时间戳排序子评论的索引,索引过期时间设置为一天
        redisCache.setCacheZSetValue(frontChildIndexCacheKey, guestbookIdToString, guestbook.getTimestamp(), BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.DAYS);
        //添加子评论数据,数据过期的时间设置为两天
        redisCache.setCacheMapValue(frontChildListCacheKey, guestbookIdToString, guestbook);
        redisCache.expire(frontChildListCacheKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
        //不是根留言则获取rootId 0 为根留言 和 回复留言id 0 为直接回复根留言
        //根据rootId更新根评论的回复数量
        guestbookMapper.updateReplyCount(rootId);
        //更新缓存的根评论的回复数量 和 更新总的评论数量
        String frontChildCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
        redisCache.hashIncrement(frontChildCacheKey, rootId.toString());
        redisCache.expire(frontChildCacheKey, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
        String frontListCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY;
        redisCache.incrementIfPresent(frontListCountCacheKey);
        redisCache.expire(frontListCountCacheKey, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
        //根据rootId获取根评论的信息进行更新根评论的热度
        Guestbook rootGuestbook = guestbookMapper.getGuestbookMessageById(rootId);
        //更新根评论的热度
        double score = HotScoreUtils.calculateHotScore(rootGuestbook.getCreateTime(), rootGuestbook.getReplyCount());
        //按照热度排序根评论的索引
        redisCache.setCacheZSetValue(BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + GuestbookConstants.ROOT_ID, rootId.toString(), score, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
        return guestbook;
    }

    @Override
    public int updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto) {
        int i = guestbookMapper.updateGuestbookMessageStatus(guestbookStatusDto);
        if (i > 0) {
            //异步操作
            AsyncManager.me().execute(GuestbookAsyncFactory.updateGuestbookMessageStatus(guestbookStatusDto));
        }
        return i;
    }

    @Override
    public int deleteGuestbookMessage(Long id) {
        //查询留言信息以及子留言信息
        Guestbook guestbook = guestbookMapper.getGuestbookMessageById(id);
        List<Guestbook> guestbookList = new ArrayList<>();
        //如果是根留言,将一起把子留言也删除
        if (GuestbookConstants.IS_ROOT.equals(guestbook.getIsRoot())) {
            //将根留言id和子留言id存储到数组中
            guestbookList = guestbookMapper.getChildGuestbookMessageIdsById(guestbook.getGuestbookId());
            guestbookList.add(guestbook);
            int i = guestbookMapper.deleteGuestbookMessageByIds(guestbookList);
            if (i > 0) {
                //异步操作redis
                AsyncManager.me().execute(GuestbookAsyncFactory.deleteGuestbookMessage(guestbookList));
            }
            return i;
        }
        guestbookList.add(guestbook);
        int i = guestbookMapper.deleteGuestbookMessage(id);
        //减少根评论的回复数量
        guestbookMapper.decreaseReplyCount(guestbook.getRootId());
        if (i > 0) {
            //异步操作redis
            AsyncManager.me().execute(GuestbookAsyncFactory.deleteGuestbookMessage(guestbookList));
        }
        return i;
    }

    @Override
    public AjaxResult getFrontRootGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        int limit = pageNum * pageSize - 1;
        String frontRootIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY + GuestbookConstants.ROOT_ID;
        String frontRootListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_KEY;
        String frontCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY;
        String frontChildCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
        String frontRootCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_ROOT_LIST_COUNT_KEY;
        //从redis中获取根评论的索引
        Set<Object> rootIndexCache = redisCache.getCacheZSetReverseValue(frontRootIndexCacheKey, offset, limit);
        if (rootIndexCache != null && !rootIndexCache.isEmpty()) {
            //根据索引查询hash结构中存储的根评论数据
            List<Guestbook> rootGuestbookList = redisCache.getMultiCacheMapValue(frontRootListCacheKey, rootIndexCache);
            //获取根评论数量
            Object rootCountObject = redisCache.getCacheObject(frontRootCountCacheKey);
            Integer total = rootCountObject != null ? Integer.parseInt(rootCountObject.toString()) : 0;
            //获取全部评论数量
            Object object = redisCache.getCacheObject(frontCountCacheKey);
            Integer guestbookAllCount = object != null ? Integer.parseInt(object.toString()) : 0;
            List<Guestbook> guestbookList = new ArrayList<>();
            //检查回复数量
            for (Guestbook guestbook : rootGuestbookList) {
                String guestbookId = guestbook.getGuestbookId().toString();
                Object cacheMapValue = redisCache.getCacheMapValue(frontChildCountCacheKey, guestbookId);
                int childTotal = cacheMapValue != null ? Integer.parseInt(cacheMapValue.toString()) : 0;
                //对比旧数据,如果新数据不等于旧数据则更新旧数据
                if (guestbook.getReplyCount() == null || guestbook.getReplyCount() != childTotal) {
                    guestbook.setReplyCount(childTotal);
                    redisCache.setCacheMapValue(frontRootListCacheKey, guestbookId, guestbook);
                }
                guestbookList.add(guestbook);
            }
            return success(total, guestbookAllCount, guestbookList);
        }
        return selectFrontRootGuestbookList(frontGuestbookListDto);
    }

    /**
     * 根据条件查询根留言列表  默认异步将数据缓存到redis中
     *
     * @param frontGuestbookListDto 查询条件
     * @return 查询结果
     */
    private AjaxResult selectFrontRootGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        return selectFrontRootGuestbookList(frontGuestbookListDto, true);
    }

    /**
     * 根据条件查询根留言列表  默认异步将数据缓存到redis中
     *
     * @param frontGuestbookListDto 查询条件
     * @param isAsync               是否异步操作
     * @return 查询结果
     */
    private AjaxResult selectFrontRootGuestbookList(FrontGuestbookListDto frontGuestbookListDto, @NotNull Boolean isAsync) {
        //获取全部评论数量
        Integer guestbookAllCount = guestbookMapper.getFrontRootGuestbookListCount(frontGuestbookListDto);
        PageUtils.startPage();
        List<Guestbook> rootGuestbookList = guestbookMapper.getFrontRootGuestbookList(frontGuestbookListDto);
        long total = new PageInfo<>(rootGuestbookList).getTotal();
        Integer totalInt = Integer.valueOf(String.valueOf(total));
        if (isAsync) {
            //异步操作,装填根留言和子留言到redis中
            AsyncManager.me().execute(GuestbookAsyncFactory.handleGuestbookListToRedis(rootGuestbookList, guestbookAllCount, totalInt));
        } else {
            for (Guestbook guestbookList : rootGuestbookList) {
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
            }
        }
        return success(totalInt, guestbookAllCount, rootGuestbookList);
    }

    @Override
    public AjaxResult getFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        int offset = (pageNum - 1) * pageSize;
        int limit = pageNum * pageSize - 1;
        Long rootId = frontGuestbookListDto.getGuestbookId();
        String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + rootId;
        String frontChildListCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
        String frontChildCountCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY;
        //从redis中获取子留言索引
        Set<Object> childIndexCache = redisCache.getCacheZSetValue(frontChildIndexCacheKey, offset, limit);
        if (childIndexCache != null && !childIndexCache.isEmpty()) {
            //获取子评论数量
            Object rootCountObject = redisCache.getCacheMapValue(frontChildCountCacheKey, rootId.toString());
            Integer total = rootCountObject != null ? Integer.parseInt(rootCountObject.toString()) : 0;
            //根据索引查询hash结构中存储的子评论的数据
            List<Guestbook> childGuestbookList = redisCache.getMultiCacheMapValue(frontChildListCacheKey, childIndexCache);
            //组装评论数据,根据parentId获取被回复评论的昵称
            for (Guestbook guestbook : childGuestbookList) {
                Long parentId = guestbook.getParentId();
                if (parentId != null && parentId != 0) {
                    Guestbook parentGuestbook = redisCache.getCacheMapValue(frontChildListCacheKey, parentId.toString());
                    if (parentGuestbook != null) {
                        guestbook.setParentNickname(parentGuestbook.getNickname());
                    }
                }
            }
            //没有全部留言数据 -1
            return success(total, -1, childGuestbookList);
        }
        return selectFrontChildGuestbookList(frontGuestbookListDto);
    }

    /**
     * 根据条件查询子留言列表  默认异步将数据缓存到redis中
     *
     * @param frontGuestbookListDto 查询条件
     * @return 查询结果
     */
    private AjaxResult selectFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        return selectFrontChildGuestbookList(frontGuestbookListDto, true);
    }

    /**
     * 根据条件查询子留言列表
     *
     * @param frontGuestbookListDto 查询条件
     * @param isAsync               是否异步操作
     * @return 查询结果
     */
    private AjaxResult selectFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto, @NotNull Boolean isAsync) {
        PageUtils.startPage();
        List<Guestbook> childGuestbookList = guestbookMapper.getFrontChildGuestbookList(frontGuestbookListDto);
        long total = new PageInfo<>(childGuestbookList).getTotal();
        Integer totalInt = Integer.valueOf(String.valueOf(total));
        if (isAsync) {
            //异步操作,装填子留言到Redis中
            AsyncManager.me().execute(GuestbookAsyncFactory.handleGuestbookChildListToRedis(childGuestbookList, totalInt));
        } else {
            cacheAllFrontChildGuestbookList(frontGuestbookListDto);
        }
        return success(totalInt, -1, childGuestbookList);
    }

    /**
     * 异步将全部子留言缓存到Redis中
     * @param frontGuestbookListDto 查询条件
     */
    @Override
    public void cacheAllFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        //获取全部子评论
        List<Guestbook> childGuestbookList = guestbookMapper.getFrontChildGuestbookList(frontGuestbookListDto);
        String frontCacheGuestbookChildListKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY;
        for (Guestbook guestbook : childGuestbookList) {
            String frontChildIndexCacheKey = BusinessCacheConstants.FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY + guestbook.getRootId();
            //将留言信息索引存储到Redis中,设置过期时间为1天
            redisCache.setCacheZSetValue(frontChildIndexCacheKey, guestbook.getGuestbookId().toString(), guestbook.getTimestamp(), BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.DAYS);
            //将留言信息存储到Redis中,设置过期时间为2天
            redisCache.setCacheMapValue(frontCacheGuestbookChildListKey, guestbook.getGuestbookId().toString(), guestbook);
            redisCache.expire(frontCacheGuestbookChildListKey, BusinessCacheConstants.CACHE_EXPIRE_TIME_TWO, TimeUnit.DAYS);
        }
    }

    @Override
    public List<GuestbookListVo> getRootGuestbookList(GuestbookListDto guestbookListDto) {
        return guestbookMapper.getRootGuestbookList(guestbookListDto);
    }

    @Override
    public List<GuestbookListVo> getChildGuestbookList(GuestbookListDto guestbookListDto) {
        return guestbookMapper.getChildGuestbookList(guestbookListDto);
    }

    @Override
    public void cacheAllChildGuestbookList(GuestbookListDto guestbookListDto) {
        List<GuestbookListVo> childGuestbookList = guestbookMapper.getChildGuestbookList(guestbookListDto);
        Long guestbookId = guestbookListDto.getGuestbookId();
        String cacheKey = BusinessCacheConstants.CACHE_GUESTBOOK_CHILD_LIST_KEY + guestbookId;
        for (GuestbookListVo guestbookListVo : childGuestbookList) {
            String messageTime = guestbookListVo.getMessageTime();
            //将留言信息存储到Redis中,设置过期时间为1天
//            redisCache.setCacheZSetValue(cacheKey, guestbookListVo, timestamp, Constants.CACHE_EXPIRE_ONE_DAY, TimeUnit.DAYS);
        }
    }

    /**
     * 根据时间范围筛选GuestbookListVo数据
     */
    private List<GuestbookListVo> filterByTimeRange(Set<Object> cacheValues, GuestbookListDto dto) {
        List<GuestbookListVo> result = new ArrayList<>();
        long startTime = 0;
        long endTime = Long.MAX_VALUE;
        // 解析时间范围
        if (StringUtils.isNotEmpty(dto.getStartTime())) {
            startTime = Objects.requireNonNull(DateUtils.parseDate(dto.getStartTime())).getTime();
        }
        if (StringUtils.isNotEmpty(dto.getEndTime())) {
            endTime = Objects.requireNonNull(DateUtils.parseDate(dto.getEndTime())).getTime();
        }
        //数据数据
        for (Object obj : cacheValues) {
            if (obj instanceof GuestbookListVo vo) {
                String messageTime = vo.getMessageTime();
                if (StringUtils.isNotEmpty(messageTime)) {
                    long timestamp = Objects.requireNonNull(DateUtils.parseDate(messageTime)).getTime();
                    if (timestamp >= startTime && timestamp <= endTime) {
                        result.add(vo);
                    }
                }
            }
        }
        // 按时间倒序排序（最新的在前）
        result.sort((a, b) -> {
            long timeA = Objects.requireNonNull(DateUtils.parseDate(a.getMessageTime())).getTime();
            long timeB = Objects.requireNonNull(DateUtils.parseDate(b.getMessageTime())).getTime();
            return Long.compare(timeB, timeA);
        });
        return result;
    }

    private AjaxResult success(Integer total, Integer guestbookAllCount, List<?> rootGuestbookList) {
        AjaxResult ajax = new AjaxResult(HttpStatus.SUCCESS, "查询成功");
        ajax.put("total", total);
        if (guestbookAllCount != null && guestbookAllCount > -1) {
            ajax.put("guestbookAllCount", guestbookAllCount);
        }
        ajax.put("data", rootGuestbookList);
        return ajax;
    }
}
