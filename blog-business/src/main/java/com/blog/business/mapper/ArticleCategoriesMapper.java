package com.blog.business.mapper;

import com.blog.business.domain.entity.ArticleCategories;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface ArticleCategoriesMapper {
    List<ArticleCategories> getArticleCategoriesList();

    ArticleCategories getArticleCategoriesDetailById(@Param("articleCategoriesId") Long articleCategoriesId);

    int addArticleCategories(ArticleCategories articleCategories);

    int updateArticleCategories(ArticleCategories articleCategories);

    void addArticleCount(@Param("articleCategoriesId") Long articleCategoriesId);

    void subtractArticleCount(@Param("articleCategoriesId") Long articleCategoriesId);

    Long getArticleCountByArticleCategoriesId(@Param("articleCategoriesId") Long articleCategoriesId);

    int deleteArticleCategories(@Param("ids") Long[] ids);
}
