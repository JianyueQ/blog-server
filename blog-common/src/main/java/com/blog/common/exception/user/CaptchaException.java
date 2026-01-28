package com.blog.common.exception.user;

/**
 * 验证码错误异常类
 * @author 31373
 */
public class CaptchaException extends UserException {
    public CaptchaException() {
        super("user.captcha.error",null);
    }
}
