package com.blog.common.core.domain.entity;

import com.blog.common.core.domain.BaseEntity;

/**
 * 社交链接
 * @author 31373
 */
public class SocialLink extends BaseEntity {

    private Long socialLinkId;

    private String name;

    private String url;

    private String icon;

    private String tip;

    /**
     * default 0
     */
    private Integer sortOrder;

    private Integer status;

    public Long getSocialLinkId() {
        return socialLinkId;
    }

    public void setSocialLinkId(Long socialLinkId) {
        this.socialLinkId = socialLinkId;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
