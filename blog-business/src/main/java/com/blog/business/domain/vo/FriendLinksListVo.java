package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 31373
 */
public class FriendLinksListVo implements Serializable {

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
     * 状态(0-隐藏, 1-显示,2-待同意)
     */
    private Integer status;

    /**
     *加入时间
     */
    private String joinTime;

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

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
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
                .append("status", getStatus())
                .append("joinTime", getJoinTime())
                .toString();
    }
}
