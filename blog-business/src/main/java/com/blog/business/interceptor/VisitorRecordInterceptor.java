package com.blog.business.interceptor;

import com.alibaba.fastjson2.JSON;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.rabbitmq.RabbitManager;
import com.blog.common.annotation.RateLimiter;
import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.HttpStatus;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.LimitType;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.http.UserAgentUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 限流器拦截器
 *
 * @author 31373
 */
@Component
public class VisitorRecordInterceptor implements HandlerInterceptor {

    /**
     * 本地缓存 缓存黑名单信息，设置过期时间为15分钟
     */
    public static final Cache<String, Boolean> BLACKLIST_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();
    private static final Logger log = LoggerFactory.getLogger(VisitorRecordInterceptor.class);
    /**
     * 缓存过期时间（30分钟）
     */
    private static final Integer CACHE_EXPIRE_MINUTES = 30;

    private final RedisCache redisCache;

    @Resource(name = "businessRabbitManager")
    private RabbitManager rabbitManager;

    public VisitorRecordInterceptor(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @RateLimiter(limitType = LimitType.IP)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //获取请求头中的x-client-data
//            String clientData = request.getHeader("x-client-data");
//            if (StringUtils.isEmpty(clientData)){
//                AjaxResult result = AjaxResult.error(HttpStatus.WARN, "你已被加入黑名单,请联系管理员");
//                ServletUtils.renderString(response, JSON.toJSONString(result));
//                return false;
//            }
            if (blackListCheck(request)) {
                AjaxResult result = AjaxResult.error(HttpStatus.WARN, "你已被加入黑名单,请联系管理员");
                ServletUtils.renderString(response, JSON.toJSONString(result));
                return false;
            }
        } catch (Exception e) {
            log.error("访客记录拦截器处理异常", e);
        }
        return true;
    }

    /**
     * 记录访客信息
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        String ip = IpUtils.getIpAddr(request);
//        if (shouldRecordVisitor(ip)){
//            recordVisitor(request,ip);
//        }
    }

    /**
     * 记录访客信息
     *
     * @param request 请求
     */
    private void recordVisitor(HttpServletRequest request,String ip) {
//        try {
//            VisitorRecord visitorRecord = new VisitorRecord();
//            visitorRecord.setIpaddr(ip);
//            visitorRecord.setLocation(AddressUtils.getRealAddressByIP(ip));
//            String userAgent = request.getHeader("User-Agent");
//            visitorRecord.setUserAgent(userAgent);
//            String clientData = request.getHeader("x-client-data");
//            visitorRecord.setClientData(clientData);
//            visitorRecord.setBrowser(UserAgentUtils.getBrowser(userAgent));
//            visitorRecord.setOs(UserAgentUtils.getOperatingSystem(userAgent));
//            visitorRecord.setVisitTime(DateUtils.getTime());
//            visitorRecord.setCreateTime(DateUtils.getNowDate());
//            // 异步保存访客记录
//            rabbitManager.sendVisitorRecord(visitorRecord);
//            log.debug("记录访客信息: IP={}, Location={}, Browser={}", ip, visitorRecord.getLocation(), visitorRecord.getBrowser());
//        } catch (Exception e) {
//            log.error("记录访客信息失败:", e);
//        }
    }

    /**
     * 是否记录访客信息
     *
     * @param ip IP地址
     * @return 是否记录访客信息
     */
    private boolean shouldRecordVisitor(String ip) {
        String cacheKey = getCacheKey(ip);
        return redisCache.setCacheUniqueValue(cacheKey, Boolean.TRUE.toString(), CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 获取缓存键
     *
     * @param ip IP地址
     * @return 缓存键
     */
    private String getCacheKey(String ip) {
        return CacheConstants.VISITOR_CACHE + ip;
    }


    /**
     * 黑名单校验 true 表示在黑名单中，拒绝访问 false 表示不在黑名单中，允许访问
     */
    private boolean blackListCheck(HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        // 检查是否在黑名单中
        Boolean ifPresent = BLACKLIST_CACHE.getIfPresent(ip);
        if (ifPresent != null && ifPresent) {
            // 在黑名单中，拒绝访问
            return true;
        }
        List<Object> cacheList = redisCache.getCacheList(CacheConstants.VISITOR_RECORD_BLACKLIST);
        if (cacheList != null && cacheList.contains(ip)) {
            BLACKLIST_CACHE.put(ip, true);
            return true;
        }
        return false;
    }
}
