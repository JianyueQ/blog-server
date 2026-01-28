package com.blog.framework.web.service;

import com.blog.common.constant.CacheConstants;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.exception.user.UserNotExistsException;
import com.blog.common.exception.user.UserPasswordNotMatchException;
import com.blog.common.exception.user.UserPasswordRetryLimitExceedException;
import com.blog.common.exception.user.UserTypeNotExistsException;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.StringUtils;
import com.blog.framework.security.context.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 密码服务处理
 *
 * @author 31373
 */
@Service
public class SysPasswordService {

    private final RedisCache redisCache;
    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;
    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    public SysPasswordService(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    public void validateOnAdmin(Administrators administrators) {
        //从线程上下文中获取自定义的令牌
        Authentication authentication = AuthenticationContextHolder.getAuthenticationHolder();
        MultiUserAuthenticationToken multiUserAuthenticationToken = (MultiUserAuthenticationToken) authentication;
        String userType = multiUserAuthenticationToken.getDetails().toString();
        String username = null;
        String password = null;
        if (StringUtils.isNotNull(userType)) {
            if ("admin".equals(userType)) {
                username = multiUserAuthenticationToken.getName();
                password = multiUserAuthenticationToken.getCredentials().toString();
            } else {
                throw new UserNotExistsException();
            }
        } else {
            throw new UserTypeNotExistsException();
        }

        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));
        if (retryCount == null) {
            retryCount = 0;
        }
        if (retryCount >= Integer.valueOf(maxRetryCount).intValue()) {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }
        if (!matches(administrators, password)) {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username);
        }
    }

    public String getCacheKey(String username) {
        if (StringUtils.isNotNull(username)){
            return CacheConstants.PWD_ERR_CNT_KEY + username;
        }else {
            return CacheConstants.PWD_ERR_CNT_KEY;
        }
    }

    public boolean matches(Administrators administrators, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, administrators.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (redisCache.hasKey(getCacheKey(loginName))) {
            redisCache.deleteObject(getCacheKey(loginName));
        }
    }
}
