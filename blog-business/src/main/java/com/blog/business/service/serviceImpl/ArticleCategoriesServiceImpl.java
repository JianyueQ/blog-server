package com.blog.business.service.serviceImpl;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.entity.ArticleCategories;
import com.blog.business.domain.entity.Articles;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import com.blog.business.exception.artices.ArticleCategoriesException;
import com.blog.business.mapper.ArticleCategoriesMapper;
import com.blog.business.mapper.ArticleMapper;
import com.blog.business.service.ArticleCategoriesService;
import com.blog.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 文章分类管理
 *
 * @author 31373
 */
@Service
public class ArticleCategoriesServiceImpl implements ArticleCategoriesService {

    @Autowired
    private ArticleCategoriesMapper articleCategoriesMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleCategories> getArticleCategoriesList() {
        List<ArticleCategories> articleCategoriesList = articleCategoriesMapper.getArticleCategoriesList();
        String articleCategoriesCountCache = BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT;
        //判断缓存键是否存在
        if (!redisCache.hasKey(articleCategoriesCountCache)) {
            return articleCategoriesList;
        }
        Map<String, Object> cacheMap = redisCache.getCacheMap(articleCategoriesCountCache);
        articleCategoriesList.forEach(articleCategories -> {
            Long articleCategoriesId = articleCategories.getArticleCategoriesId();
            Object articleCount = cacheMap.get(articleCategoriesId.toString());
            articleCategories.setArticleCount(articleCount == null ? 0 : Integer.parseInt(articleCount.toString()));
        });
        return articleCategoriesList;
    }

    @Override
    public ArticleCategories getArticleCategoriesDetailById(Long articleCategoriesId) {
        return articleCategoriesMapper.getArticleCategoriesDetailById(articleCategoriesId);
    }

    @Override
    public int addArticleCategories(ArticleCategories articleCategories) {
        return articleCategoriesMapper.addArticleCategories(articleCategories);
    }

    @Override
    public int updateArticleCategories(ArticleCategories articleCategories) {
        return articleCategoriesMapper.updateArticleCategories(articleCategories);
    }

    @Override
    public int deleteArticleCategories(Long[] ids) {
        //文章分类id批量查询文章
        List<Articles> articlesList = articleMapper.getArticlesListByArticleCategoriesIds(ids);
        if (articlesList != null && !articlesList.isEmpty()){
            String categoryName = articlesList.get(0).getCategoryName();
            throw new ArticleCategoriesException("分类名称:[ " + categoryName + " ]下有文章信息，请先删除文章再删除文章分类");
        }
        return articleCategoriesMapper.deleteArticleCategories(ids);
    }

    @Override
    public List<ArticleCategories> frontGetArticleCategoriesList() {
        return articleCategoriesMapper.frontGetArticleCategoriesList();
    }


    @Override
    public List<FrontArticlesPageVo> frontGetArticleListByArticleCategoriesId(Long articleCategoriesId) {
        return articleCategoriesMapper.frontGetArticleListByArticleCategoriesId(articleCategoriesId);
    }

}
