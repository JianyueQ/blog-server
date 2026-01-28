package com.blog.framework.interceptor.impl;

import com.alibaba.fastjson2.JSON;
import com.blog.common.annotation.RepeatSubmit;
import com.blog.common.constant.CacheConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.filter.RepeatedlyRequestWrapper;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.http.HttpHelper;
import com.blog.framework.interceptor.RepeatSubmitInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 *
 * @author 31373
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {

    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";
    private final RedisCache redisCache;
    @Value("${token.header}")
    private String header;

    public SameUrlDataInterceptor(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 判断请求是否重复
     *
     * @param request      请求信息
     * @param repeatSubmit 防重复注解参数
     * @return true：重复提交 false：正常提交
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit) {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper repeatedlyRequest) {
            nowParams = HttpHelper.getBodyString(request);
        }
        //参数为空，获取Parameter的数据
        if (StringUtils.isEmpty(nowParams)) {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());
        //唯一值:请求地址（作为存放cache的key值）
        String url = request.getRequestURI();
        //唯一值:（没有消息头则使用请求地址）
        String submitKey = StringUtils.trimToEmpty(request.getHeader(header));
        // 唯一标识（指定key + url + 消息头）
        String cacheRepeatKey = CacheConstants.REPEAT_SUBMIT_KEY + url + submitKey;
        //从redis中获取map,查看是否为同一个请求
        Object sessionObj = redisCache.getCacheObject(cacheRepeatKey);
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, repeatSubmit.interval())) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        redisCache.setCacheObject(cacheRepeatKey, cacheMap, repeatSubmit.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    /**
     * 判断两次间隔时间
     *
     * @param nowDataMap 现在请求参数
     * @param preDataMap 上次请求参数
     * @param interval   时间间隔
     * @return true：间隔时间小于interval false：间隔时间大于interval
     */
    private boolean compareTime(Map<String, Object> nowDataMap, Map<String, Object> preDataMap, int interval) {
        long time1 = (Long) nowDataMap.get(REPEAT_TIME);
        long time2 = (Long) preDataMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }

    /**
     * 判断请求参数
     *
     * @param nowDataMap 现在请求参数
     * @param preDataMap 上次请求参数
     * @return true：参数相同 false：参数不同
     */
    private boolean compareParams(Map<String, Object> nowDataMap, Map<String, Object> preDataMap) {
        Object nowParams = nowDataMap.get(REPEAT_PARAMS);
        Object preParams = preDataMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }
}
