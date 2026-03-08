package com.blog.business.controller.admin;

import com.blog.business.domain.entity.ArticleCategories;
import com.blog.business.service.ArticleCategoriesService;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章分类管理
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/articleCategories")
public class ArticleCategoriesController extends BaseController {

    @Autowired
    private ArticleCategoriesService articleCategoriesService;

    /**
     * 文章分类列表
     */
    @GetMapping("/list")
    public TableDataInfo getArticleCategoriesList() {
        startPage();
        List<ArticleCategories> list = articleCategoriesService.getArticleCategoriesList();
        return getDataTable(list);
    }

    /**
     * 文章分类详情
     */
    @GetMapping("/detail")
    public AjaxResult getArticleCategoriesDetail(Long articleCategoriesId) {
        ArticleCategories articleCategories = articleCategoriesService.getArticleCategoriesDetailById(articleCategoriesId);
        return AjaxResult.success(articleCategories);
    }

    /**
     * 添加文章分类
     */
    @PostMapping("/add")
    public AjaxResult addArticleCategories(@RequestBody ArticleCategories articleCategories) {
        return toAjax(articleCategoriesService.addArticleCategories(articleCategories));
    }

    /**
     * 修改文章分类
     */
    @PostMapping("/update")
    public AjaxResult updateArticleCategories(@RequestBody ArticleCategories articleCategories) {
        return toAjax(articleCategoriesService.updateArticleCategories(articleCategories));
    }

    /**
     * 删除文章分类
     */
    @PostMapping("/delete/{ids}")
    public AjaxResult deleteArticleCategories(@PathVariable("ids") Long[] ids) {
        return toAjax(articleCategoriesService.deleteArticleCategories(ids));
    }


}
