package com.blog.system.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 社交联系信息
 * @author 31373
 */
public class SocialLinkVo implements Serializable {

    private String socialLinkId;

    private String name;

    private String icon;

    private String tip;

    private String url;

    private String sortOrder;

    private String status;

    public String getSocialLinkId() {
        return socialLinkId;
    }

    public void setSocialLinkId(String socialLinkId) {
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

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("name", getName())
                .append("icon", getIcon())
                .append("tip", getTip())
                .append("url", getUrl())
                .append("status", getStatus())
                .append("sortOrder", getSortOrder())
                .toString();
    }
}
