package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 前台用户文章VO
 * @author 31373
 */
public class FrontArticlesVo implements Serializable {

    private String title;

    private String slug;

    private Integer publishDay;

    private String publishTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getPublishDay() {
        return publishDay;
    }

    public void setPublishDay(Integer publishDay) {
        this.publishDay = publishDay;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("title", getTitle())
                .append("slug", getSlug())
                .append("publishDay", getPublishDay())
                .append("publishTime", getPublishTime())
                .toString();
    }
}
