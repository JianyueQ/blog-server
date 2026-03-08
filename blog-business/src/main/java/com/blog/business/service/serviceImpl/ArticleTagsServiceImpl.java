package com.blog.business.service.serviceImpl;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.business.domain.entity.ArticleTags;
import com.blog.business.domain.entity.Articles;
import com.blog.business.exception.artices.ArticleCategoriesException;
import com.blog.business.mapper.ArticleTagsMapper;
import com.blog.business.service.ArticleTagsService;
import com.blog.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 31373
 */
@Service
public class ArticleTagsServiceImpl implements ArticleTagsService {

    @Autowired
    private ArticleTagsMapper articleTagsMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public List<ArticleTags> getArticleTagsList() {
        List<ArticleTags> articleTagsList = articleTagsMapper.getArticleTagsList();
        String tagFrequency = BusinessCacheConstants.TAG_FREQUENCY;
        //判断缓存键是否存在
        if (!redisCache.hasKey(tagFrequency)) {
            return articleTagsList;
        }
        Map<String, Object> cacheMap = redisCache.getCacheMap(tagFrequency);
        articleTagsList.forEach(articleTags -> {
            Long articleTagsId = articleTags.getArticleTagsId();
            Object articleCount = cacheMap.get(articleTagsId.toString());
            articleTags.setUsageFrequency(articleCount == null ? 0 : Integer.parseInt(articleCount.toString()));
        });
        return articleTagsList;
    }

    @Override
    public ArticleTags getArticleTagsDetail(Long articleTagsId) {
        return articleTagsMapper.getArticleTagsDetail(articleTagsId);
    }

    @Override
    public int addArticleTags(ArticleTags articleTags) {
        return articleTagsMapper.addArticleTags(articleTags);
    }

    @Override
    public int editArticleTags(ArticleTags articleTags) {
        return articleTagsMapper.editArticleTags(articleTags);
    }

    @Override
    public int deleteArticleTags(Long[] articleTagsId) {
        //根据文章id查询文章关联的标签
        List<ArticleTagRelations> articleTagRelationsList = articleTagsMapper.getArticleTagsRelationsByArticleTagsId(articleTagsId);
        if (articleTagRelationsList != null && !articleTagRelationsList.isEmpty()){
            String tagName = articleTagRelationsList.get(0).getName();
            throw new ArticleCategoriesException("标签:[ " + tagName + " ]下有文章信息，请先删除文章再删除标签");
        }
        return articleTagsMapper.deleteArticleTags(articleTagsId);
    }



}
