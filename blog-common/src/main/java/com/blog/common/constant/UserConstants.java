package com.blog.common.constant;

/**
 * 用户相关常量
 *
 * @author 31373
 */
public class UserConstants {

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;

    /**
     * 校验是否唯一的返回标识
     */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";
    /**
     * 是否为系统默认（否）
     */
    public static final String NO = "N";

    /**
     * 正常访问
     */
    public static final Integer NORMAL_ACCESS = 0;

    /**
     * 异常访问
     */
    public static final Integer EXCEPTION_ACCESS = 1;
}
