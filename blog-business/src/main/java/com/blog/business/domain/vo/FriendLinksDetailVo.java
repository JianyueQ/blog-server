package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 友链详情
 * @author 31373
 */
public class FriendLinksDetailVo {

    /**
     * 站点id
     */
    private Long friendLinksId;

    /**
     * 站点名称
     */
    private String name;

    /**
     * 站点地址
     */
    private String url;
    /**
     * 站点logo
     */
    private String logo;
    /**
     * 站点描述
     */
    private String description;
    /**
     * 联系邮箱
     */
    private String email;

    public Long getFriendLinksId() {
        return friendLinksId;
    }

    public void setFriendLinksId(Long friendLinksId) {
        this.friendLinksId = friendLinksId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("friendLinksId", getFriendLinksId())
                .append("name", getName())
                .append("url", getUrl())
                .append("logo", getLogo())
                .append("description", getDescription())
                .append("email", getEmail())
                .toString();
    }
}
