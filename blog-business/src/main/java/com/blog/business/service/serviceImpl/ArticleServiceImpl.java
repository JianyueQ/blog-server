package com.blog.business.service.serviceImpl;

import com.blog.business.constant.ArticlesConstant;
import com.blog.business.domain.dto.ArticleListDto;
import com.blog.business.domain.entity.Articles;
import com.blog.business.domain.vo.FrontArticlesArchivesVo;
import com.blog.business.domain.vo.FrontArticlesDetailVo;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import com.blog.business.domain.vo.FrontArticlesVo;
import com.blog.business.exception.artices.ArticlesException;
import com.blog.business.manager.factory.ArticlesAsyncFactory;
import com.blog.business.mapper.ArticleMapper;
import com.blog.business.mapper.ArticleTagsMapper;
import com.blog.business.service.ArticleService;
import com.blog.business.utils.FingerprintUtils;
import com.blog.business.utils.MarkdownUtil;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import com.blog.framework.manager.AsyncManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 文章管理
 *
 * @author 31373
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagsMapper articleTagsMapper;
    @Autowired
    private RedisCache redisCache;


    @Override
    public List<Articles> getArticleList(Articles articles) {
        return articleMapper.getArticleList(articles);
    }

    @Override
    public Articles getArticleDetail(Long articlesId) {
        Articles articleDetail = articleMapper.getArticleDetail(articlesId);
        //根据文章id查询关联的标签
        articleDetail.setArticleTagRelations(articleTagsMapper.getArticleTagsListByArticlesId(articlesId));
        return articleDetail;
    }

    @Override
    @CacheEvict(cacheNames = "frontArticles", allEntries = true)
    public int addArticle(Articles articles) {
        if (articles.getArticleCategoriesId() == null || articles.getArticleCategoriesId() == 0) {
            throw new ArithmeticException("文章id不能为空");
        }
        if (StringUtils.isEmpty(articles.getCategoryName())) {
            throw new ArithmeticException("文章分类名称不能为空");
        }
        if (StringUtils.isEmpty(articles.getTitle())) {
            throw new ArithmeticException("文章标题不能为空");
        }
        if (StringUtils.isEmpty(articles.getSlug())) {
            throw new ArithmeticException("url标识不能为空");
        }
        if (StringUtils.isEmpty(articles.getContentMarkdown())) {
            throw new ArithmeticException("文章内容不能为空");
        }
        String contentMarkdown = articles.getContentMarkdown();
        // 统计字数
        articles.setWordCount(MarkdownUtil.countWords(contentMarkdown));
        // 计算预计阅读时间
        articles.setReadingTime(MarkdownUtil.calculateReadingTime(contentMarkdown));
        // 转换为 HTML
        articles.setContentHtml(MarkdownUtil.toHtml(contentMarkdown));
        articles.setPublishTime(DateUtils.getNowDate());
        articles.setIsPublished(ArticlesConstant.IS_NOT_PUBLISHED);
        articles.setIsTop(ArticlesConstant.IS_NOT_TOP);
        int i = articleMapper.addArticle(articles);
        if (i > 0) {
            //异步进行耗时操作
            AsyncManager.me().execute(ArticlesAsyncFactory.articlesAddHandler(articles));
        }
        return i;
    }

    @Override
    @CacheEvict(cacheNames = "frontArticles", allEntries = true)
    public int editArticle(Articles articles) {
        if (articles.getArticleCategoriesId() == null || articles.getArticleCategoriesId() == 0) {
            throw new ArticlesException("文章id不能为空");
        }
        if (StringUtils.isEmpty(articles.getCategoryName())) {
            throw new ArticlesException("文章分类名称不能为空");
        }
        if (StringUtils.isEmpty(articles.getTitle())) {
            throw new ArticlesException("文章标题不能为空");
        }
        if (StringUtils.isEmpty(articles.getSlug())) {
            throw new ArticlesException("url标识不能为空");
        }
        if (StringUtils.isEmpty(articles.getContentMarkdown())) {
            throw new ArticlesException("文章内容不能为空");
        }
        String contentMarkdown = articles.getContentMarkdown();
        // 统计字数
        articles.setWordCount(MarkdownUtil.countWords(contentMarkdown));
        // 计算预计阅读时间
        articles.setReadingTime(MarkdownUtil.calculateReadingTime(contentMarkdown));
        // 转换为 HTML
        articles.setContentHtml(MarkdownUtil.toHtml(contentMarkdown));
        articles.setIsPublished(ArticlesConstant.IS_NOT_PUBLISHED);
        articles.setIsTop(ArticlesConstant.IS_NOT_TOP);
        //根据文章id查询数据库获取旧的分类id
        Long oldArticleCategoriesId = articleMapper.getArticleCategoriesIdByArticlesId(articles.getArticlesId());
        int i = articleMapper.editArticle(articles);
        if (i > 0) {
            AsyncManager.me().execute(ArticlesAsyncFactory.articlesEditHandler(articles, oldArticleCategoriesId));
        }
        return i;
    }

    @Override
    @CacheEvict(cacheNames = "frontArticles", allEntries = true)
    public int deleteArticles(Long[] articlesIds) {
        //批量获取文章数据
        List<Articles> articlesList = articleMapper.getArticlesListByIds(articlesIds);
        int i = articleMapper.deleteArticles(articlesIds);
        if (i > 0) {
            AsyncManager.me().execute(ArticlesAsyncFactory.articlesDeleteHandler(articlesIds, articlesList));
        }
        return i;
    }

    @Override
    @CacheEvict(cacheNames = "frontArticles", allEntries = true)
    public int changePublishStatus(Long articlesId, Integer isPublished, Integer isTop) {
        if (articlesId == null) {
            throw new ArticlesException("文章id不能为空");
        }
        return articleMapper.changePublishStatus(articlesId, isPublished, isTop);
    }

    @Override
    @Cacheable(cacheNames = "frontArticles", keyGenerator = "CacheKeyGenerator")
    public List<FrontArticlesPageVo> frontGetArticleList(ArticleListDto articleListDto) {
        return articleMapper.frontGetArticleList();
    }

    @Override
    @Cacheable(cacheNames = "frontArticles", keyGenerator = "CacheKeyGenerator")
    public FrontArticlesDetailVo frontGetArticleDetail(String slug) {
        return articleMapper.frontGetArticleDetail(slug);
    }

    @Override
    @Cacheable(cacheNames = "frontArticles", key = "'archives'")
    public List<FrontArticlesArchivesVo> frontGetArticleArchives() {
        List<Articles> articlesList = articleMapper.frontGetArticleArchives();
        List<FrontArticlesArchivesVo> result = new ArrayList<>();
        // 用于临时存储按年月分组的数据
        Map<String, List<FrontArticlesVo>> yearMonthMap = new HashMap<>();
        for (Articles articles : articlesList) {
            Integer year = articles.getPublishYear();
            Integer month = articles.getPublishMonth();
            if (year == null || month == null) {
                continue;
            }
            String key = year + "-" + month;
            // 如果该分组不存在，创建新的分组
            yearMonthMap.computeIfAbsent(key, k -> new ArrayList<>());
            // 创建文章 VO 对象
            FrontArticlesVo frontArticlesVo = new FrontArticlesVo();
            frontArticlesVo.setTitle(articles.getTitle());
            frontArticlesVo.setSlug(articles.getSlug());
            frontArticlesVo.setPublishDay(articles.getPublishDay());
            frontArticlesVo.setPublishTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, articles.getPublishTime()));
            yearMonthMap.get(key).add(frontArticlesVo);
        }
        // 将分组数据转换为 VO 列表
        for (Map.Entry<String, List<FrontArticlesVo>> entry : yearMonthMap.entrySet()) {
            String[] parts = entry.getKey().split("-");
            Integer year = Integer.valueOf(parts[0]);
            Integer month = Integer.valueOf(parts[1]);
            FrontArticlesArchivesVo archivesVo = new FrontArticlesArchivesVo();
            archivesVo.setYear(year);
            archivesVo.setMonth(month);
            archivesVo.setArticlesList(entry.getValue());
            result.add(archivesVo);
        }
        return result;
    }

    @Override
    public void addArticleBrowseNum(String slug, HttpServletRequest request) {
        String cacheKey = ArticlesConstant.ARTICLES_BROWSE_NUM_KEY + slug;
        long one = ArticlesConstant.ONE;
        String ipAddr = IpUtils.getIpAddr(request);
        if (redisCache.setCacheUniqueValue(cacheKey + ipAddr, ipAddr,one, TimeUnit.DAYS)){
            redisCache.increment(cacheKey,one, TimeUnit.DAYS);
        }
    }
}
