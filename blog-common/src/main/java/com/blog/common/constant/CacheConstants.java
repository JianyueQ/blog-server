package com.blog.common.constant;

/**
 * 缓存的key 常量
 * @author 31373
 */
public class CacheConstants {

    /**
     * 重复提交缓存 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码缓存 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";
    /**
     * 登录密码错误次数缓存 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 登录令牌缓存 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
}
