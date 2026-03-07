package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;

/**
 * @author 31373
 */
public class VisitorInfo extends BaseEntity {

    /**
     * 访客信息id
     */
    private Long visitorInfoId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像地址
     */
    private String avatar;

    public Long getVisitorInfoId() {
        return visitorInfoId;
    }

    public void setVisitorInfoId(Long visitorInfoId) {
        this.visitorInfoId = visitorInfoId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
