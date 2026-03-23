package com.blog.business.service;

import com.blog.business.domain.dto.ArticleListDto;
import com.blog.business.domain.entity.Articles;
import com.blog.business.domain.vo.FrontArticlesArchivesVo;
import com.blog.business.domain.vo.FrontArticlesDetailVo;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 文章管理
 * @author 31373
 */
public interface ArticleService {
    List<Articles> getArticleList(Articles articles);


    Articles getArticleDetail(Long articlesId);

    int addArticle(Articles articles);

    int editArticle(Articles articles);

    int deleteArticles(Long[] articlesId);

    int changePublishStatus(Long articlesId, Integer isPublished, Integer isTop);

    List<FrontArticlesPageVo> frontGetArticleList(ArticleListDto articleListDto);

    FrontArticlesDetailVo frontGetArticleDetail(String slug);

    List<FrontArticlesArchivesVo> frontGetArticleArchives();

    void addArticleBrowseNum(String slug, HttpServletRequest request);
}
