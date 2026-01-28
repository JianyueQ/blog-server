package com.blog.framework.web.service;

import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.UserConstants;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.domain.model.LoginUserOnUser;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.exception.ServiceException;
import com.blog.common.exception.user.CaptchaException;
import com.blog.common.exception.user.CaptchaExpireException;
import com.blog.common.exception.user.UserNotExistsException;
import com.blog.common.exception.user.UserPasswordNotMatchException;
import com.blog.common.utils.StringUtils;
import com.blog.framework.security.context.AuthenticationContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 系统登录服务实现类
 *
 * @author 31373
 */
@Service
public class SysLoginService {


    private final RedisCache redisCache;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public SysLoginService(RedisCache redisCache, AuthenticationManager authenticationManager, TokenService tokenService) {

        this.redisCache = redisCache;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public String login(String username, String password, String code, String uuid, String userType) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            //使用自定义令牌
            MultiUserAuthenticationToken authenticationToken = new MultiUserAuthenticationToken(username, password, userType);
            AuthenticationContextHolder.setAuthenticationHolder(authenticationToken);
            //调用自定义用户认证的方法
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        } finally {
            AuthenticationContextHolder.clearAuthenticationHolder();
        }
        recordLoginInfo(authentication);
        // 生成token
        return tokenService.createToken(authentication);
    }

    public void recordLoginInfo(Authentication authentication) {
        //todo 记录登录信息
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        //todo IP黑名单校验
//        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
//        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
//        {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
//            throw new BlackListException();
//        }
    }

    /**
     * 验证码校验
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null) {
            //todo 异步记录登录日志
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        redisCache.deleteObject(verifyKey);
        if (!code.equalsIgnoreCase(captcha)) {
            //todo 异步记录登录日志
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }
}
