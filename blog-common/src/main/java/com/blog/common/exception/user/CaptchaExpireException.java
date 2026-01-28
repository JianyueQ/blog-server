package com.blog.common.exception.user;

import java.io.Serial;

/**
 * 验证码失效异常类
 * @author 31373
 */
public class CaptchaExpireException extends UserException {
  @Serial
  private static final long serialVersionUID = 1L;

  public CaptchaExpireException()
  {
    super("user.captcha.expire", null);
  }
}
