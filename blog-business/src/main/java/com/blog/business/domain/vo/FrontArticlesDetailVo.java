package com.blog.business.domain.vo;

import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章表
 * @author 31373
 */
public class FrontArticlesDetailVo implements Serializable {
    /**
     * 文章ID
     */
    @JsonIgnore
    private Long articlesId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * url标识，如：what-is-slug-field
     */
    private String slug;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 封面图片url
     */
    private String coverImage;
    /**
     * markdown内容
     */
    private String contentMarkdown;
    /**
     * 转换后的html内容
     */
    private String contentHtml;

    /**
     * 文章分类id
     */
    @JsonIgnore
    private Long articleCategoriesId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 浏览次数
     */
    private Integer viewCount;
    /**
     * 点赞次数
     */
    private Integer likeCount;
    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 字数统计
     */
    private Integer wordCount;
    /**
     * 预计阅读时间，单位：分钟
     */
    private Integer readingTime;
    /**
     * 是否发布,0-否，1-是
     */
    private Integer isPublished;
    /**
     * 是否置顶,0-否，1-是
     */
    private Integer isTop;
    /**
     * 发布时间
     */
    private Date publishTime;
    /**
     * 关联的文章标签
     */
    private List<ArticleTagRelations> articleTagRelations;

    public Long getArticlesId() {
        return articlesId;
    }

    public void setArticlesId(Long articlesId) {
        this.articlesId = articlesId;
    }

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getContentMarkdown() {
        return contentMarkdown;
    }

    public void setContentMarkdown(String contentMarkdown) {
        this.contentMarkdown = contentMarkdown;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public Long getArticleCategoriesId() {
        return articleCategoriesId;
    }

    public void setArticleCategoriesId(Long articleCategoriesId) {
        this.articleCategoriesId = articleCategoriesId;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Integer readingTime) {
        this.readingTime = readingTime;
    }

    public Integer getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ArticleTagRelations> getArticleTagRelations() {
        return articleTagRelations;
    }

    public void setArticleTagRelations(List<ArticleTagRelations> articleTagRelations) {
        this.articleTagRelations = articleTagRelations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("articlesId", getArticlesId())
                .append("title", getTitle())
                .append("slug", getSlug())
                .append("summary", getSummary())
                .append("coverImage", getCoverImage())
                .append("contentMarkdown", getContentMarkdown())
                .append("contentHtml", getContentHtml())
                .append("articleCategoriesId", getArticleCategoriesId())
                .append("categoryName", getCategoryName())
                .append("viewCount", getViewCount())
                .append("likeCount", getLikeCount())
                .append("commentCount", getCommentCount())
                .append("wordCount", getWordCount())
                .append("readingTime", getReadingTime())
                .append("isPublished", getIsPublished())
                .append("isTop", getIsTop())
                .append("publishTime", getPublishTime())
                .append("articleTagRelations", getArticleTagRelations())
                .toString();
    }
}
