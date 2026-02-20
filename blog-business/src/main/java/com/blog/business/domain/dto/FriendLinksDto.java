package com.blog.business.domain.dto;

import com.blog.common.xss.Xss;

/**
 * 友链参数
 * @author 31373
 */
public class FriendLinksDto {

    /**
     * 站点id
     */
    private Long friendLinksId;

    /**
     * 站点名称
     */
    @Xss(message = "站点名称包含非法字符")
    private String name;

    /**
     * 站点地址
     */
    @Xss(message = "站点地址包含非法字符")
    private String url;
    /**
     * 站点logo
     */
    @Xss(message = "站点logo包含非法字符")
    private String logo;
    /**
     * 站点描述
     */
    @Xss(message = "站点描述包含非法字符")
    private String description;

    /**
     * 联系邮箱
     */
    @Xss(message = "联系邮箱包含非法字符")
    private String email;
    /**
     * 状态(0-隐藏, 1-显示,2-待通过)
     */
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
