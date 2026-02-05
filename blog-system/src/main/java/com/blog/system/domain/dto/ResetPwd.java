package com.blog.system.domain.dto;

/**
 * 重置密码
 * @author 31373
 */
public class ResetPwd {

    private Long adminId;

    private String password;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
