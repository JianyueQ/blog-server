package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 友链表
 * @author 31373
 */
public class FriendLinks extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

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
    /**
     * 状态(0-隐藏, 1-显示)
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sortOrder;

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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
