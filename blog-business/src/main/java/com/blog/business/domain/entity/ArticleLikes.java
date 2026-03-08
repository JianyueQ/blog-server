package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;

/**
 * @author 31373
 */
public class ArticleLikes extends BaseEntity {
    /**
     * 文章点赞ID
     */
    private Integer articleLikesId;
    /**
     * 文章ID
     */
    private Integer articleId;
    /**
     * 访客ID
     */
    private Integer visitorId;
    /**
     * 点赞状态：0-取消点赞，1-点赞
     */
    private Integer likeStatus;

}
