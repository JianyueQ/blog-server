package com.blog.business.service;

import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.business.domain.entity.ArticleTags;

import java.util.List;

/**
 * 文章标签管理
 * @author 31373
 */
public interface ArticleTagsService {
    List<ArticleTags> getArticleTagsList();

    ArticleTags getArticleTagsDetail(Long articleTagsId);

    int addArticleTags(ArticleTags articleTags);

    int editArticleTags(ArticleTags articleTags);

    int deleteArticleTags(Long[] articleTagsId);

}
