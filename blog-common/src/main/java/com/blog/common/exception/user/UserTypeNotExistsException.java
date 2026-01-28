package com.blog.common.exception.user;

/**
 * 用户类型不存在异常
 * @author 31373
 */
public class UserTypeNotExistsException extends UserException {
    public UserTypeNotExistsException() {
        super("user.type.not.exists",null);
    }
}
