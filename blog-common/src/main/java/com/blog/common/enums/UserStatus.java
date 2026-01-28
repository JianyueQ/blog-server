package com.blog.common.enums;

/**
 * 用户状态
 *
 * @author 31373
 */
public enum UserStatus {

    OK("0", "正常"), DELETE("1", "删除");

    private final String code;
    private final String info;


    UserStatus(String code, String info){
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

}
