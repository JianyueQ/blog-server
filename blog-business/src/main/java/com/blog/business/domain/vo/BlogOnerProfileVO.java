package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;

/**
 * 博主个人信息
 *
 * @author 31373
 */
public class BlogOnerProfileVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String uuid;
    private String email;
    private String nickname;
    private String avatar;
    private String description;
    private String lastLoginTime;
    private String lastLoginIp;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("uuid", getUuid())
                .append("email", getEmail())
                .append("nickname", getNickname())
                .append("avatar", getAvatar())
                .append("description", getDescription())
                .append("lastLoginTime", getLastLoginTime())
                .append("lastLoginIp", getLastLoginIp())
                .toString();
    }
}
