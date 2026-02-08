package com.blog.common.exception.user;

import java.io.Serial;

/**
 * 用户不存在异常类
 *
 * @author 31373
 */
public class UserNotExistsException extends UserException
{
    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotExistsException()
    {
        super("user.not.exists", null);
    }
}
