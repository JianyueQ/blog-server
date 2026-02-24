package com.blog.business.annotation;

import com.blog.business.enums.MessageRecordType;

import java.lang.annotation.*;

/**
 * 发送消息注解
 *
 * @author 31373
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SendMessage {

    /**
     * 消息标题
     *
     * @return 消息标题
     */
    String messageTitle() default "";

    /**
     * 消息类型
     *
     * @return 消息类型
     */
    MessageRecordType messageType() default MessageRecordType.SYSTEM;

    /**
     * 消息内容
     *
     * @return 消息内容
     */
    String messageContent() default "";

    /**
     * 是否广播
     *
     * @return 是否广播
     */
    boolean broadcast() default true;

    /**
     * 接收用户的用户名
     *
     * @return 接收用户的用户名
     */
    String[] receiveUsernames() default {};

}
