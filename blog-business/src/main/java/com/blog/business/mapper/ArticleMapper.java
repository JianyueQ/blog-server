package com.blog.business.mapper;

import com.blog.business.domain.entity.Articles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章管理
 * @author 31373
 */
@Mapper
public interface ArticleMapper {
    List<Articles> getArticleList(Articles articles);

    Articles getArticleDetail(@Param("articlesId") Long articlesId);

    int addArticle(Articles articles);

    int editArticle(Articles articles);

    int deleteArticles(@Param("articlesIds") Long[] articlesIds);

    int changePublishStatus(@Param("articlesId") Long articlesId, @Param("isPublished") Integer isPublished, @Param("isTop") Integer isTop);

    Long getArticleCategoriesIdByArticlesId(@Param("articlesId") Long articlesId);

    List<Articles> getArticlesListByArticleCategoriesIds(@Param("articleCategoriesIds") Long[] ids);

    List<Articles> getArticlesListByIds(@Param("articlesIds") Long[] articlesIds);
}
