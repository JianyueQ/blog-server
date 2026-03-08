package com.blog.business.service.serviceImpl;

import com.blog.business.constant.ArticlesConstant;
import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.business.domain.entity.Articles;
import com.blog.business.exception.artices.ArticlesException;
import com.blog.business.manager.factory.ArticlesAsyncFactory;
import com.blog.business.mapper.ArticleCategoriesMapper;
import com.blog.business.mapper.ArticleMapper;
import com.blog.business.mapper.ArticleTagsMapper;
import com.blog.business.service.ArticleService;
import com.blog.business.utils.MarkdownUtil;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.StringUtils;
import com.blog.framework.manager.AsyncManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public int addArticle(Articles articles) {
        if (articles.getArticleCategoriesId() == null || articles.getArticleCategoriesId() == 0){
            throw new ArithmeticException("文章id不能为空");
        }
        if (StringUtils.isEmpty(articles.getCategoryName())){
            throw new ArithmeticException("文章分类名称不能为空");
        }
        if (StringUtils.isEmpty(articles.getTitle())){
            throw new ArithmeticException("文章标题不能为空");
        }
        if (StringUtils.isEmpty(articles.getSlug())){
            throw new ArithmeticException("url标识不能为空");
        }
        if (StringUtils.isEmpty(articles.getContentMarkdown())){
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
        if (i>0){
            //异步进行耗时操作
            AsyncManager.me().execute(ArticlesAsyncFactory.articlesAddHandler(articles));
        }
        return i;
    }

    @Override
    @Transactional
    public int editArticle(Articles articles) {
        if (articles.getArticleCategoriesId() == null || articles.getArticleCategoriesId() == 0){
            throw new ArticlesException("文章id不能为空");
        }
        if (StringUtils.isEmpty(articles.getCategoryName())){
            throw new ArticlesException("文章分类名称不能为空");
        }
        if (StringUtils.isEmpty(articles.getTitle())){
            throw new ArticlesException("文章标题不能为空");
        }
        if (StringUtils.isEmpty(articles.getSlug())){
            throw new ArticlesException("url标识不能为空");
        }
        if (StringUtils.isEmpty(articles.getContentMarkdown())){
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
        if (i>0){
            AsyncManager.me().execute(ArticlesAsyncFactory.articlesEditHandler(articles,oldArticleCategoriesId));
        }
        return i;
    }

    @Override
    public int deleteArticles(Long[] articlesIds) {
        //批量获取文章数据
        List<Articles> articlesList = articleMapper.getArticlesListByIds(articlesIds);
        int i = articleMapper.deleteArticles(articlesIds);
        if (i>0){
            AsyncManager.me().execute(ArticlesAsyncFactory.articlesDeleteHandler(articlesIds,articlesList));
        }
        return i;
    }

    @Override
    public int changePublishStatus(Long articlesId, Integer isPublished, Integer isTop) {
        if (articlesId == null){
            throw new ArticlesException("文章id不能为空");
        }
        return articleMapper.changePublishStatus(articlesId, isPublished, isTop);
    }
}
