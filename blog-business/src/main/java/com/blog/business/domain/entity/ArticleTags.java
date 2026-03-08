package com.blog.business.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author 31373
 */
public class ArticleTags {

    /**
     * 文章标签ID
     */
    private Long articleTagsId;
    /**
     * 标签名称
     */
    private String name;
    /**
     * url标识
     */
    private String slug;

    /**
     * 使用频次
     */
    private Integer usageFrequency;

    public Long getArticleTagsId() {
        return articleTagsId;
    }

    public void setArticleTagsId(Long articleTagsId) {
        this.articleTagsId = articleTagsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getUsageFrequency() {
        return usageFrequency;
    }

    public void setUsageFrequency(Integer usageFrequency) {
        this.usageFrequency = usageFrequency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("articleTagsId", getArticleTagsId())
                .append("name", getName())
                .append("slug", getSlug())
                .append("usageFrequency", getUsageFrequency())
                .toString();
    }
}
