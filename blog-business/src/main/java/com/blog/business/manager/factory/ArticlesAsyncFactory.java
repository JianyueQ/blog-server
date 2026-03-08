package com.blog.business.manager.factory;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.business.domain.entity.Articles;
import com.blog.business.mapper.ArticleCategoriesMapper;
import com.blog.business.mapper.ArticleTagsMapper;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 文章异步方法
 *
 * @author 31373
 */
public class ArticlesAsyncFactory {

    private static final Logger sys_articles_logger = LoggerFactory.getLogger("sys-articles");


    /**
     * 文章添加操作处理器
     */
    public static TimerTask articlesAddHandler(final Articles articles) {
        return new TimerTask() {
            @Override
            public void run() {
                //获取bean
                ArticleTagsMapper articleTagsMapper = SpringUtils.getBean(ArticleTagsMapper.class);
                ArticleCategoriesMapper articleCategoriesMapper = SpringUtils.getBean(ArticleCategoriesMapper.class);
                RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
                List<ArticleTagRelations> articleTagRelations = articles.getArticleTagRelations();
                for (ArticleTagRelations articleTagRelation : articleTagRelations) {
                    articleTagRelation.setArticleId(articles.getArticlesId());
                }
                //批量添加关联标签
                articleTagsMapper.addArticleTagsRelationsByList(articleTagRelations);
                //根据分类id获取该分类的文章数量
                Long articleCategoriesId = articles.getArticleCategoriesId();
                Long ifPresent = redisCache.hashIncrementIfPresent(BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT, articleCategoriesId.toString());
                if (ifPresent == null) {
                    //查询数据库获取数量
                    Long articleCount = articleCategoriesMapper.getArticleCountByArticleCategoriesId(articleCategoriesId);
                    redisCache.hashIncrement(BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT, articleCategoriesId.toString(), articleCount + 1);
                    redisCache.expire(BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                } else {
                    redisCache.expire(BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                }
                //批量更新标签的使用频率
                for (ArticleTagRelations articleTagRelation : articleTagRelations) {
                    //判断标签使用频率是否存在缓存中，存在则加1
                    Long tagId = articleTagRelation.getTagId();
                    String tagFrequencyCache = BusinessCacheConstants.TAG_FREQUENCY;
                    Long ifPresent1 = redisCache.hashIncrementIfPresent(tagFrequencyCache, tagId.toString());
                    if (ifPresent1 == null) {
                        //查询数据库获取使用频率
                        Long tagFrequency = articleTagsMapper.getTagFrequencyByTagId(articleTagRelation.getTagId());
                        redisCache.hashIncrement(tagFrequencyCache, tagId.toString(), tagFrequency + 1);
                        redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else {
                        redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    }
                }
            }
        };
    }

    /**
     * 文章修改处理器
     *
     * @param articles 文章信息
     * @return TimerTask
     */
    public static TimerTask articlesEditHandler(final Articles articles, final Long oldArticleCategoriesId) {
        return new TimerTask() {
            @Override
            public void run() {
                //获取bean
                ArticleTagsMapper articleTagsMapper = SpringUtils.getBean(ArticleTagsMapper.class);
                ArticleCategoriesMapper articleCategoriesMapper = SpringUtils.getBean(ArticleCategoriesMapper.class);
                RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

                Long newArticleCategoriesId = articles.getArticleCategoriesId();
                if (!oldArticleCategoriesId.equals(newArticleCategoriesId)) {
                    String articleCategoriesCountCache = BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT;
                    Long ifPresent = redisCache.hashDecrementIfPresent(articleCategoriesCountCache, oldArticleCategoriesId.toString());
                    if (ifPresent == null) {
                        //查询数据库获取数量
                        Long articleCount = articleCategoriesMapper.getArticleCountByArticleCategoriesId(oldArticleCategoriesId);
                        if (articleCount > 0) {
                            redisCache.hashIncrement(articleCategoriesCountCache, oldArticleCategoriesId.toString(), articleCount - 1);
                        } else {
                            redisCache.setCacheMapValue(articleCategoriesCountCache, oldArticleCategoriesId.toString(), 0);
                        }
                        redisCache.expire(articleCategoriesCountCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else if (ifPresent > 0) {
                        redisCache.expire(articleCategoriesCountCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else {
                        redisCache.setCacheMapValue(articleCategoriesCountCache, oldArticleCategoriesId.toString(), 0);
                    }
                    Long ifPresent1 = redisCache.hashIncrementIfPresent(articleCategoriesCountCache, newArticleCategoriesId.toString());
                    if (ifPresent1 == null) {
                        Long articleCount = articleCategoriesMapper.getArticleCountByArticleCategoriesId(newArticleCategoriesId);
                        redisCache.hashIncrement(articleCategoriesCountCache, newArticleCategoriesId.toString(), articleCount + 1);
                        redisCache.expire(articleCategoriesCountCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else {
                        redisCache.expire(articleCategoriesCountCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    }
                }
                Long articlesId = articles.getArticlesId();
                //获取全部文章关联的标签
                List<ArticleTagRelations> oldArticleTagRelations = articleTagsMapper.getArticleTagsRelationsByArticleId(articlesId);
                //提取旧标签 ID 集合
                Set<Long> oldTagIds = new HashSet<>();
                for (ArticleTagRelations oldRelation : oldArticleTagRelations) {
                    oldTagIds.add(oldRelation.getTagId());
                }
                //提取新标签 ID 集合
                Set<Long> newTagIds = new HashSet<>();
                //删除全部文章关联的标签
                articleTagsMapper.deleteArticleTagsRelationsByArticleId(articlesId);
                if (articles.getArticleTagRelations() != null && !articles.getArticleTagRelations().isEmpty()) {
                    List<ArticleTagRelations> newArticleTagRelationsList = articles.getArticleTagRelations();
                    for (ArticleTagRelations articleTagRelations : newArticleTagRelationsList) {
                        articleTagRelations.setArticleId(articlesId);
                        newTagIds.add(articleTagRelations.getTagId());
                    }
                    //批量添加关联标签
                    articleTagsMapper.addArticleTagsRelationsByList(newArticleTagRelationsList);
                    //计算新增的标签 (新标签有，旧标签没有)
                    Set<Long> addTagIds = new HashSet<>(newTagIds);
                    addTagIds.removeAll(oldTagIds);

                    //计算删除的标签 (旧标签有，新标签没有)
                    Set<Long> removeTagIds = new HashSet<>(oldTagIds);
                    removeTagIds.removeAll(newTagIds);
                    //增加新增标签的使用频率
                    for (Long tagId : addTagIds) {
                        String tagFrequencyCache = BusinessCacheConstants.TAG_FREQUENCY;
                        Long ifPresent = redisCache.hashIncrementIfPresent(tagFrequencyCache, tagId.toString());
                        if (ifPresent == null) {
                            //查询数据库获取使用频率
                            Long tagFrequency = articleTagsMapper.getTagFrequencyByTagId(tagId);
                            redisCache.hashIncrement(tagFrequencyCache, tagId.toString(), tagFrequency + 1);
                            redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                        } else {
                            redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                        }
                    }
                    //减少删除标签的使用频率
                    for (Long tagId : removeTagIds) {
                        String tagFrequencyCache = BusinessCacheConstants.TAG_FREQUENCY;
                        Long ifPresent = redisCache.hashDecrementIfPresent(tagFrequencyCache, tagId.toString());
                        if (ifPresent == null) {
                            //查询数据库获取使用频率
                            Long tagFrequency = articleTagsMapper.getTagFrequencyByTagId(tagId);
                            if (tagFrequency > 0) {
                                redisCache.hashDecrement(tagFrequencyCache, tagId.toString(), tagFrequency - 1);
                            } else {
                                redisCache.setCacheMapValue(tagFrequencyCache, tagId.toString(), 0);
                            }
                            redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                        } else if (ifPresent > 0) {
                            redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                        } else {
                            redisCache.setCacheMapValue(tagFrequencyCache, tagId.toString(), 0);
                        }
                    }
                } else {
                    //减少标签使用频率
                    for (ArticleTagRelations oldArticleTagRelation : oldArticleTagRelations) {
                        //判断标签使用频率是否存在缓存中，存在则加1
                        Long tagId = oldArticleTagRelation.getTagId();
                        String tagFrequencyCache = BusinessCacheConstants.TAG_FREQUENCY;
                        Long ifPresent1 = redisCache.hashDecrementIfPresent(tagFrequencyCache, tagId.toString());
                        if (ifPresent1 == null) {
                            //查询数据库获取使用频率
                            Long tagFrequency = articleTagsMapper.getTagFrequencyByTagId(oldArticleTagRelation.getTagId());
                            if (tagFrequency > 0) {
                                redisCache.hashDecrement(tagFrequencyCache, tagId.toString(), tagFrequency - 1);
                            } else {
                                redisCache.setCacheMapValue(tagFrequencyCache, tagId.toString(), 0);
                            }
                            redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                        } else if (ifPresent1 > 0) {
                            redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                        } else {
                            redisCache.setCacheMapValue(tagFrequencyCache, tagId.toString(), 0);
                        }
                    }
                }
            }
        };
    }

    public static TimerTask articlesDeleteHandler(final Long[] articlesIds, final List<Articles> articlesList) {
        return new TimerTask() {
            @Override
            public void run() {
                ArticleTagsMapper articleTagsMapper = SpringUtils.getBean(ArticleTagsMapper.class);
                ArticleCategoriesMapper articleCategoriesMapper = SpringUtils.getBean(ArticleCategoriesMapper.class);
                RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

                //批量获取文章关联的标签
                List<ArticleTagRelations> articleTagsRelations = articleTagsMapper.getArticleTagsRelationsByArticleIds(articlesIds);
                //批量删除文章关联的标签
                articleTagsMapper.deleteArticleTagsRelationsByArticleIds(articlesIds);
                String articleCategoriesCountCache = BusinessCacheConstants.ARTICLE_CATEGORIES_ARTICLE_COUNT;
                //减少文章分类下的文章数量
                for (Articles articles : articlesList) {
                    Long articleCategoriesId = articles.getArticleCategoriesId();
                    Long ifPresent = redisCache.hashDecrementIfPresent(articleCategoriesCountCache, articleCategoriesId.toString());
                    if (ifPresent == null) {
                        Long articleCount = articleCategoriesMapper.getArticleCountByArticleCategoriesId(articleCategoriesId);
                        if (articleCount > 0) {
                            redisCache.hashIncrement(articleCategoriesCountCache, articleCategoriesId.toString(), articleCount - 1);
                        } else {
                            redisCache.hashIncrement(articleCategoriesCountCache, articleCategoriesId.toString(), 0);
                        }
                        redisCache.expire(articleCategoriesCountCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else if (ifPresent > 0) {
                        redisCache.expire(articleCategoriesCountCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else {
                        redisCache.setCacheMapValue(articleCategoriesCountCache, articleCategoriesId.toString(), 0);
                    }
                }
                //统计每个标签的使用频率 (被多少篇文章使用)
                Map<Long, Long> tagFrequencyMap = new HashMap<>();
                for (ArticleTagRelations relation : articleTagsRelations) {
                    Long tagId = relation.getTagId();
                    tagFrequencyMap.put(tagId, tagFrequencyMap.getOrDefault(tagId, 0L) + 1);
                }
                //批量减少标签的使用频率
                for (Map.Entry<Long, Long> entry : tagFrequencyMap.entrySet()) {
                    Long tagId = entry.getKey();
                    Long frequency = entry.getValue();

                    String tagFrequencyCache = BusinessCacheConstants.TAG_FREQUENCY;
                    Long ifPresent = redisCache.hashDecrementIfPresent(tagFrequencyCache, tagId.toString());
                    if (ifPresent == null) {
                        //查询数据库获取使用频率
                        Long tagFrequency = articleTagsMapper.getTagFrequencyByTagId(tagId);
                        if (tagFrequency > frequency) {
                            redisCache.hashDecrement(tagFrequencyCache, tagId.toString(), tagFrequency - frequency);
                        } else {
                            redisCache.setCacheMapValue(tagFrequencyCache, tagId.toString(), 0);
                        }
                        redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else if (ifPresent > 0) {
                        redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    } else {
                        redisCache.setCacheMapValue(tagFrequencyCache, tagId.toString(), 0);
                        redisCache.expire(tagFrequencyCache, BusinessCacheConstants.CACHE_EXPIRE_TIME_ONE, TimeUnit.HOURS);
                    }
                }
            }
        };
    }
}
