package com.blog.common.enums;

/**
 * 用户类型
 * @author 31373
 */

public enum UserType {

    ADMIN("admin"),USER("user");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}
