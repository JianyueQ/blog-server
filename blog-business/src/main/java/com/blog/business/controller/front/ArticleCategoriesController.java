package com.blog.business.controller.front;

import com.blog.business.domain.entity.ArticleCategories;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import com.blog.business.service.ArticleCategoriesService;
import com.blog.common.annotation.Anonymous;
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
@Anonymous
@RestController("frontArticleCategoriesController")
@RequestMapping("/blog/articleCategories")
public class ArticleCategoriesController extends BaseController {

    @Autowired
    private ArticleCategoriesService articleCategoriesService;

    /**
     * 前台用户获取文章分类列表
     */
    @GetMapping("/getArticleCategoriesList")
    public TableDataInfo getArticleCategoriesList() {
        startPage();
        List<ArticleCategories> list = articleCategoriesService.frontGetArticleCategoriesList();
        return getDataTable(list);
    }

    /**
     * 前台用户根据文章分类获取文章列表
     */
    @GetMapping("/getArticleListByArticleCategoriesId")
    public TableDataInfo getArticleListByArticleCategoriesId(@RequestParam("articleCategoriesId") Long articleCategoriesId) {
        startPage();
        List<FrontArticlesPageVo> list = articleCategoriesService.frontGetArticleListByArticleCategoriesId(articleCategoriesId);
        return getDataTable(list);
    }

}
