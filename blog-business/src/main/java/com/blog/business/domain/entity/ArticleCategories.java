package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author 31373
 */
public class ArticleCategories extends BaseEntity {
    /**
     * 文章分类ID
     */
    private Long articleCategoriesId;
    /**
     * 分类名称,如：日常,心得,年度总结,编程,面经
     */
    private String name;
    /**
     * URL标识，如：daily, thinking, year-summary, programming, interview
     */
    private String slug;
    /**
     * 分类描述
     */
    private String description;
    /**
     * 文章数量
     */
    private Integer articleCount;
    /**
     * 排序，越小越靠前
     */
    private Integer sort;

    public Long getArticleCategoriesId() {
        return articleCategoriesId;
    }

    public void setArticleCategoriesId(Long articleCategoriesId) {
        this.articleCategoriesId = articleCategoriesId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("articleCategoriesId", getArticleCategoriesId())
                .append("name", getName())
                .append("slug", getSlug())
                .append("description", getDescription())
                .append("articleCount", getArticleCount())
                .append("sort", getSort())
                .toString();
    }
}
