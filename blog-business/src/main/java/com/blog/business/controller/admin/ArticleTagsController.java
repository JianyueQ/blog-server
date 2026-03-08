package com.blog.business.controller.admin;

import com.blog.business.domain.entity.ArticleTagRelations;
import com.blog.business.domain.entity.ArticleTags;
import com.blog.business.service.ArticleTagsService;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章标签管理
 * @author 31373
 */
@RestController
@RequestMapping("/system/articleTags")
public class ArticleTagsController extends BaseController {

    @Autowired
    private ArticleTagsService articleTagsService;

    /**
     * 获取文章标签列表
     */
    @GetMapping("/list")
    public TableDataInfo getArticleTagsList() {
        startPage();
        List<ArticleTags> list = articleTagsService.getArticleTagsList();
        return getDataTable(list);
    }

    /**
     * 获取文章标签详情
     */
    @GetMapping("/detail")
    public AjaxResult getArticleTagsDetail(Long articleTagsId) {
        ArticleTags articleTags = articleTagsService.getArticleTagsDetail(articleTagsId);
        return AjaxResult.success(articleTags);
    }

    /**
     * 添加文章标签
     */
    @PostMapping("/add")
    public AjaxResult addArticleTags(@RequestBody ArticleTags articleTags) {
        return toAjax(articleTagsService.addArticleTags(articleTags));
    }

    /**
     * 修改文章标签
     */
    @PostMapping("/edit")
    public AjaxResult editArticleTags(@RequestBody ArticleTags articleTags) {
        return toAjax(articleTagsService.editArticleTags(articleTags));
    }

    /**
     * 删除文章标签
     */
    @PostMapping("/delete/{ids}")
    public AjaxResult deleteArticleTags(@PathVariable("ids") Long[] articleTagsId) {
        return toAjax(articleTagsService.deleteArticleTags(articleTagsId));
    }
}
