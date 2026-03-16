package com.blog.business.task;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.entity.ArticleCategories;
import com.blog.business.domain.entity.ArticleTags;
import com.blog.business.mapper.ArticleCategoriesMapper;
import com.blog.business.mapper.ArticleTagsMapper;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.spring.SpringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文章任务
 *
 * @author 31373
 */
@Component
public class ArticleTask {


    private static final Logger log = LogManager.getLogger(ArticleTask.class);

    /**
     * 将文章分类下的文章数量缓存同步到数据库(30分钟)
     */
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void syncArticleCountToDatabase() {
        ArticleCategoriesMapper articleCategoriesMapper = SpringUtils.getBean(ArticleCategoriesMapper.class);
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        //获取缓存中所有的文章分类下的文章数量
        Map<String, Object> cacheMap = redisCache.getCacheMap(BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT);
        List<ArticleCategories> articleCategoriesList = new ArrayList<>();
        if (!cacheMap.isEmpty()){
            cacheMap.forEach((key, value) -> {
                ArticleCategories articleCategories = new ArticleCategories();
                articleCategories.setArticleCount(Integer.parseInt(value.toString()));
                articleCategories.setArticleCategoriesId(Long.parseLong(key));
                articleCategoriesList.add(articleCategories);
            });
            log.debug("将文章分类下的文章数量缓存同步到数据库...");
            articleCategoriesMapper.updateArticleCount(articleCategoriesList);
        }
    }

    /**
     * 将标签的使用频率同步到数据库中 (30分钟)
     */
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void syncTagFrequencyToDatabase() {
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        ArticleTagsMapper articleTagsMapper = SpringUtils.getBean(ArticleTagsMapper.class);
        Map<String, Object> cacheMap = redisCache.getCacheMap(BusinessCacheConstants.TAG_FREQUENCY);
        List<ArticleTags> articleTagsList = new ArrayList<>();
        if (!cacheMap.isEmpty()){
            cacheMap.forEach((key, value) -> {
                ArticleTags articleTags = new ArticleTags();
                articleTags.setUsageFrequency(Integer.parseInt(value.toString()));
                articleTags.setArticleTagsId(Long.parseLong(key));
                articleTagsList.add(articleTags);
            });
            log.debug("将标签的使用频率同步到数据库中...");
            articleTagsMapper.updateTagFrequency(articleTagsList);
        }
    }
}
