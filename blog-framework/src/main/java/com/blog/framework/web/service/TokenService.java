package com.blog.framework.web.service;

import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.Constants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.domain.model.LoginUserOnUser;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.enums.UserType;
import com.blog.common.exception.user.UserTypeNotExistsException;
import com.blog.common.utils.ServletUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.http.UserAgentUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import com.blog.common.utils.uuid.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * 获取用户身份信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    public LoginUserOnUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUserOnUser user = redisCache.getCacheObject(userKey);
                return user;
            } catch (Exception e) {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从请求头中获取token
     *
     * @param request 请求对象
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 删除管理员用户信息
     *
     * @param token 管理员用户信息
     */
    public void delLoginUserOnAdmin(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 获取管理员用户信息
     *
     * @param request 请求对象
     * @return 管理员用户信息
     */
    public LoginUserOnAdmin getLoginUserOnAdmin(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                LoginUserOnAdmin user = redisCache.getCacheObject(userKey);
                return user;
            } catch (Exception e) {
                log.error("获取管理员用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUserOnAdmin 管理员用户信息
     */
    public void verifyToken(LoginUserOnAdmin loginUserOnAdmin) {
        long expireTime = loginUserOnAdmin.getExpireTime();
        long currentTime = System.currentTimeMillis();
        long remainingTime = expireTime - currentTime;
        if (remainingTime <= TWENTY_MINUTES) {
            log.debug("Token即将过期，剩余时间: {}分钟，开始刷新", remainingTime / 1000 / 60);
            refreshTokenOnAdmin(loginUserOnAdmin);
        }
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUserOnAdmin(LoginUserOnAdmin loginUserOnAdmin)
    {
        if (StringUtils.isNotNull(loginUserOnAdmin) && StringUtils.isNotEmpty(loginUserOnAdmin.getToken()))
        {
            refreshTokenOnAdmin(loginUserOnAdmin);
        }
    }
}
