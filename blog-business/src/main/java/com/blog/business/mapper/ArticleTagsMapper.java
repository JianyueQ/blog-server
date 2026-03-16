package com.blog.business.mapper;

import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.business.domain.entity.ArticleTags;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface ArticleTagsMapper {

    List<ArticleTags> getArticleTagsList();

    ArticleTags getArticleTagsDetail(@Param("articleTagsId") Long articleTagsId);

    int addArticleTags(ArticleTags articleTags);

    int editArticleTags(ArticleTags articleTags);

    int deleteArticleTags(@Param("articleTagsId") Long[] articleTagsId);

    List<ArticleTagRelations> getArticleTagsListByArticlesId(@Param("articlesId") Long articlesId);

    void addArticleTagsRelationsByList(@Param("articleTagRelationsList") List<ArticleTagRelations> articleTagRelationsList);

    void batchUpdateArticleTagsUsageFrequency(@Param("articleTagRelations") List<ArticleTagRelations> articleTagRelations);

    void deleteArticleTagsRelationsByArticleId(Long articlesId);

    Long getTagFrequencyByTagId(@Param("tagId") Long tagId);

    List<ArticleTagRelations> getArticleTagsRelationsByArticleId(@Param("articlesId") Long articlesId);

    List<ArticleTagRelations> getArticleTagsRelationsByArticleIds(@Param("articlesIds") Long[] articlesIds);

    void deleteArticleTagsRelationsByArticleIds(@Param("articlesIds") Long[] articlesIds);

    List<ArticleTagRelations> getArticleTagsRelationsByArticleTagsId(@Param("articleTagsId") Long[] articleTagsId);

    List<ArticleTags> frontGetArticleTagsList();

    void updateTagFrequency(@Param("articleTagsList") List<ArticleTags> articleTagsList);

    List<FrontArticlesPageVo> frontGetArticleListByTag(@Param("slug") String slug);
}
