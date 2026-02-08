package com.blog.common.constant;

/**
 * RabbitMQ常量类
 * @author 31373
 */
public class RabbitMqConstants {

    /**
     * 管理员详情交换机
     */
    public static final String ADMIN_DETAIL_EXCHANGE = "admin.details.exchange";

    /**
     * 管理员登录日志 routing key
     */
    public static final String ADMIN_LOGIN_LOG_KEY = "admin.login.log";

    /**
     * 管理员登录日志处理队列
     */
    public static final String ADMIN_LOGIN_LOG_QUEUE = "admin.login.log.queue";

    /**
     * 管理员登录信息 routing key
     */
    public static final String ADMIN_LOGIN_KEY = "admin.login";

    /**
     * 管理员登录信息处理队列
     */
    public static final String ADMIN_LOGIN_QUEUE = "admin.login.queue";
}
