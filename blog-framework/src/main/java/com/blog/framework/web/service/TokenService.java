package com.blog.framework.web.service;

import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.Constants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.domain.model.LoginUserOnUser;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.enums.UserType;
import com.blog.common.exception.user.UserTypeNotExistsException;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.http.UserAgentUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import com.blog.common.utils.uuid.IdUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Component
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final RedisCache redisCache;
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;
    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;
    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    public TokenService(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 1秒
     */
    protected static final long ONE_SECOND = 1000;

    /**
     * 1分钟
     */
    protected static final long ONE_MINUTE = 60 * ONE_SECOND;

    /**
     * 20分钟
     */
    private static final Long TWENTY_MINUTES = 20 * 60 * 1000L;

    /**
     * 创建令牌
     *
     * @param authentication 认证信息
     * @return 令牌
     */
    public String createToken(Authentication authentication) {
        String userType = String.valueOf(authentication.getDetails());
        String token = IdUtils.fastUUID();
        if (UserType.ADMIN.getUserType().equals(userType)) {
            LoginUserOnAdmin loginUserOnAdmin = (LoginUserOnAdmin) authentication.getPrincipal();
            loginUserOnAdmin.setToken(token);
            setUserAgentOnAdmin(loginUserOnAdmin);
            refreshTokenOnAdmin(loginUserOnAdmin);
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.LOGIN_USER_KEY, token);
            claims.put(Constants.JWT_USERNAME, loginUserOnAdmin.getUsername());
            return createToken(claims);
        } else if (UserType.USER.getUserType().equals(userType)) {
            LoginUserOnUser loginUserOnUser = (LoginUserOnUser) authentication.getPrincipal();
            loginUserOnUser.setToken(token);
            refreshTokenOnUser(loginUserOnUser);
            setUserAgentOnUser(loginUserOnUser);
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.LOGIN_USER_KEY, token);
            claims.put(Constants.JWT_USERNAME, loginUserOnUser.getUsername());
            return createToken(claims);
        }else {
            throw new UserTypeNotExistsException();
        }
    }

    /**
     * 创建令牌
     * @param claims 负载
     * @return 令牌
     */
    public String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 设置用户代理信息
     * @param loginUserOnUser 用户信息
     */
    public void setUserAgentOnUser(LoginUserOnUser loginUserOnUser) {
        String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        String ip = IpUtils.getIpAddr();
        loginUserOnUser.setIpaddr(ip);
        loginUserOnUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUserOnUser.setBrowser(UserAgentUtils.getBrowser(userAgent));
        loginUserOnUser.setOs(UserAgentUtils.getOperatingSystem(userAgent));
    }

    /**
     * 设置用户代理信息
     * @param loginUserOnAdmin 用户信息
     */
    public void setUserAgentOnAdmin(LoginUserOnAdmin loginUserOnAdmin) {
        String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        String ip = IpUtils.getIpAddr();
        loginUserOnAdmin.setIpaddr(ip);
        loginUserOnAdmin.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUserOnAdmin.setBrowser(UserAgentUtils.getBrowser(userAgent));
        loginUserOnAdmin.setOs(UserAgentUtils.getOperatingSystem(userAgent));
    }


    /**
     * 刷新令牌
     * @param loginUserOnUser 用户信息
     */
    public void refreshTokenOnUser(LoginUserOnUser loginUserOnUser) {
        loginUserOnUser.setLoginTime(System.currentTimeMillis());
        loginUserOnUser.setExpireTime(System.currentTimeMillis() + expireTime * ONE_MINUTE);
        String tokenKey = getTokenKey(loginUserOnUser.getToken());
        redisCache.setCacheObject(tokenKey, loginUserOnUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 刷新令牌
     * @param loginUserOnAdmin 用户信息
     */
    public void refreshTokenOnAdmin(LoginUserOnAdmin loginUserOnAdmin) {
        loginUserOnAdmin.setLoginTime(System.currentTimeMillis());
        loginUserOnAdmin.setExpireTime(loginUserOnAdmin.getLoginTime() + expireTime * ONE_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUserOnAdmin.getToken());
        redisCache.setCacheObject(userKey, loginUserOnAdmin, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String uuid)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
