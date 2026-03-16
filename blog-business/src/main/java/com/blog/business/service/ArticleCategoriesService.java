package com.blog.business.service;

import com.blog.business.domain.entity.ArticleCategories;
import com.blog.business.domain.vo.FrontArticlesPageVo;

import java.util.List;

/**
 * 文章分类管理
 * @author 31373
 */
public interface ArticleCategoriesService {
    List<ArticleCategories> getArticleCategoriesList();

    ArticleCategories getArticleCategoriesDetailById(Long articleCategoriesId);

    int addArticleCategories(ArticleCategories articleCategories);

    int updateArticleCategories(ArticleCategories articleCategories);

    int deleteArticleCategories(Long[] ids);

    List<ArticleCategories> frontGetArticleCategoriesList();

    List<FrontArticlesPageVo> frontGetArticleListByArticleCategoriesId(Long articleCategoriesId);

}
