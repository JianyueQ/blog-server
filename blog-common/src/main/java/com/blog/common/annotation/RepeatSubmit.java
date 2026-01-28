package com.blog.common.annotation;

import java.lang.annotation.*;

/**
 * 防止重复提交注解
 *
 * @author 31373
 */
@Inherited //表示该注解具有继承性
@Target({ElementType.METHOD}) //表示该注解只能用于方法
@Retention(RetentionPolicy.RUNTIME) //表示该注解在运行时保留
@Documented // 表示该注解会生成文档
public @interface RepeatSubmit {

    /**
     * 间隔时间,小于这个间隔时间被视为重复提交
     */
    public int interval() default 5000;

    /**
     * 提示消息
     */
    public String message() default "请勿重复提交";
}
