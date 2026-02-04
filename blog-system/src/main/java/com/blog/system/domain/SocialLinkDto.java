package com.blog.system.domain;

/**
 * 社交联系信息
 * @author 31373
 */
public class SocialLinkDto {

    private Long socialLinkId;

    private String name;

    private String icon;

    private String tip;

    private String url;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
