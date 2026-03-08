package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文章标签关联表
 * @author 31373
 */
public class ArticleTagRelations extends BaseEntity {

    /**
     * 文章标签关系ID
     */
    private Long articleTagRelationsId;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 标签名称
     */
    private String name;
    /**
     * url标识
     */
    private String slug;

    public Long getArticleTagRelationsId() {
        return articleTagRelationsId;
    }

    public void setArticleTagRelationsId(Long articleTagRelationsId) {
        this.articleTagRelationsId = articleTagRelationsId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
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

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("articleTagRelationsId", getArticleTagRelationsId())
                .append("articleId", getArticleId())
                .append("tagId", getTagId())
                .append("name", getName())
                .append("slug", getSlug())
                .toString();
    }
}
