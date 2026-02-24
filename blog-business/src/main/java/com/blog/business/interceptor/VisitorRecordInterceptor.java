package com.blog.business.interceptor;

import com.alibaba.fastjson2.JSON;
import com.blog.business.annotation.SendMessage;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.rabbitmq.RabbitManager;
import com.blog.common.annotation.RateLimiter;
import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.HttpStatus;
import com.blog.common.constant.UserConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.LimitType;
import com.blog.common.exception.ServiceException;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.http.UserAgentUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
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

    private static final Logger log = LoggerFactory.getLogger(VisitorRecordInterceptor.class);

    /**
     * 缓存过期时间（15分钟）
     */
    private static final Integer CACHE_EXPIRE_MINUTES = 15;

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
            // 获取访客信息
            String ip = IpUtils.getIpAddr(request);
            List<Object> cacheList = redisCache.getCacheList(CacheConstants.VISITOR_RECORD_BLACKLIST);
            if (cacheList != null && cacheList.contains(ip)) {
                AjaxResult result = AjaxResult.error(HttpStatus.WARN, "你已被加入黑名单,请联系管理员");
                ServletUtils.renderString(response, JSON.toJSONString(result));
                return false;
            }
            // 检查是否在规定时间内已记录
            if (shouldRecordVisitor(ip)) {
                // 记录访客信息
                recordVisitor(request, ip);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("访客记录拦截器处理异常", e);
        }
        return true;
    }


    /**
     * 记录访客信息
     *
     * @param request 请求
     * @param ip      IP地址
     */
    private void recordVisitor(HttpServletRequest request, String ip) {
        try {
            VisitorRecord visitorRecord = new VisitorRecord();
            visitorRecord.setIpaddr(ip);
            visitorRecord.setLocation(AddressUtils.getRealAddressByIP(ip));

            String userAgent = request.getHeader("User-Agent");
            visitorRecord.setUserAgent(userAgent);
            visitorRecord.setBrowser(UserAgentUtils.getBrowser(userAgent));
            visitorRecord.setOs(UserAgentUtils.getOperatingSystem(userAgent));
            visitorRecord.setVisitTime(DateUtils.getTime());
            // 默认正常访问 0
            visitorRecord.setAbnormalFlag(UserConstants.NORMAL_ACCESS);
            visitorRecord.setCreateBy(ip);
            // 异步保存访客记录
            rabbitManager.sendVisitorRecord(visitorRecord);
            log.debug("记录访客信息: IP={}, Location={}, Browser={}", ip, visitorRecord.getLocation(), visitorRecord.getBrowser());

        } catch (Exception e) {
            log.error("记录访客信息失败: IP={}", ip, e);
        }
    }

    /**
     * 是否记录访客信息
     *
     * @param ip IP地址
     * @return 是否记录访客信息
     */
    private boolean shouldRecordVisitor(String ip) {
        String cacheKey = getCacheKey(ip);
        return redisCache.setCacheUniqueValue(cacheKey, ip, CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
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
}
