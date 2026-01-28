package com.blog.common.constant;

import java.util.Locale;

/**
 * 通用常量信息
 *
 * @author 31373
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 内容类型 JSON
     */
    public static final String CONTENT_TYPE_JSON = "application/json";
    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;
    /**
     * 令牌
     */
    public static final String TOKEN = "token";
    /**
     * 系统语言
     */
    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * JWT 用户名
     */
    public static final String JWT_USERNAME = "username";
}
