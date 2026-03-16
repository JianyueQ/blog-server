package com.blog.business.controller.admin;

import com.blog.business.domain.entity.Articles;
import com.blog.business.service.ArticleService;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章管理
 * @author 31373
 */
@RestController("adminArticlesController")
@RequestMapping("/system/articles")
public class ArticlesController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     */
    @GetMapping("/list")
    public TableDataInfo getArticleList(Articles articles) {
        startPage();
        List<Articles> list = articleService.getArticleList(articles);
        return getDataTable(list);
    }

    /**
     * 查看文章详情
     */
    @GetMapping("/detail")
    public AjaxResult getArticleDetail(@RequestParam("articlesId") Long articlesId) {
        Articles articles = articleService.getArticleDetail(articlesId);
        return AjaxResult.success(articles);
    }

    /**
     * 新增文章
     */
    @PostMapping("/add")
    public AjaxResult addArticle(@RequestBody Articles articles) {
        return toAjax(articleService.addArticle(articles));
    }

    /**
     * 修改文章
     */
    @PostMapping("/edit")
    public AjaxResult editArticle(@RequestBody Articles articles) {
        return toAjax(articleService.editArticle(articles));
    }

    /**
     * 删除文章
     */
    @PostMapping("/delete/{ids}")
    public AjaxResult deleteArticle(@PathVariable("ids") Long[] articlesId) {
        return toAjax(articleService.deleteArticles(articlesId));
    }

    /**
     * 修改文章发布状态
     */
    @PostMapping("/changePublishStatus")
    public AjaxResult changePublishStatus(@RequestBody Articles articles) {
        return toAjax(articleService.changePublishStatus(articles.getArticlesId(), articles.getIsPublished(), articles.getIsTop()));
    }




}
