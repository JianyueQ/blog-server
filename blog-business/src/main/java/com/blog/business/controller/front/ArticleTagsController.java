package com.blog.business.controller.front;

import com.blog.business.domain.entity.ArticleTags;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import com.blog.business.service.ArticleTagsService;
import com.blog.common.annotation.Anonymous;
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
@Anonymous
@RestController("frontArticleTagsController")
@RequestMapping("/blog/articleTags")
public class ArticleTagsController extends BaseController {

    @Autowired
    private ArticleTagsService articleTagsService;

    /**
     * 获取标签列表
     */
    @GetMapping("/getAllTags")
    public TableDataInfo getAllTags() {
        startPage();
        List<ArticleTags> list = articleTagsService.frontGetArticleTagsList();
        return getDataTable(list);
    }

    /**
     * 根据标签获取文章列表
     */
    @GetMapping("/getArticleListByTag")
    public TableDataInfo getArticleListByTag(@RequestParam("slug") String slug) {
        startPage();
        List<FrontArticlesPageVo> list = articleTagsService.frontGetArticleListByTag(slug);
        return getDataTable(list);
    }

}
