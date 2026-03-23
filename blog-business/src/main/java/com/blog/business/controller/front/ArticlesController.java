package com.blog.business.controller.front;

import com.blog.business.domain.dto.ArticleListDto;
import com.blog.business.domain.vo.FrontArticlesArchivesVo;
import com.blog.business.domain.vo.FrontArticlesDetailVo;
import com.blog.business.domain.vo.FrontArticlesPageVo;
import com.blog.business.service.ArticleService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章管理
 *
 * @author 31373
 */
@Anonymous
@RestController("frontArticlesController")
@RequestMapping("/blog/articles")
public class ArticlesController extends BaseController {

    @Autowired
    private ArticleService articleService;

    /**
     * 前台用户获取文章列表
     */
    @GetMapping("/getArticleList")
    public TableDataInfo frontGetArticleList(ArticleListDto articleListDto) {
        startPage();
        List<FrontArticlesPageVo> list = articleService.frontGetArticleList(articleListDto);
        return getDataTable(list);
    }

    /**
     * 查看文章详情
     */
    @GetMapping("/getArticleDetail/{slug}")
    public AjaxResult frontGetArticleDetail(@PathVariable("slug") String slug) {
        FrontArticlesDetailVo articles = articleService.frontGetArticleDetail(slug);
        return AjaxResult.success(articles);
    }

    /**
     * 前台用户获取文章归档
     */
    @GetMapping("/getArticleArchives")
    public AjaxResult frontGetArticleArchives() {
        List<FrontArticlesArchivesVo> list = articleService.frontGetArticleArchives();
        return AjaxResult.success(list);
    }

    /**
     * 添加文章浏览数量
     */
    @PostMapping("/addArticleBrowseNum/{slug}")
    public AjaxResult addArticleBrowseNum(@PathVariable("slug") String slug) {
        Long viewNum = articleService.addArticleBrowseNum(slug);
        return AjaxResult.success(viewNum);
    }

    /**
     * 文章点赞
     */
    @PostMapping("/addArticleLikeNum/{slug}")
    public AjaxResult addArticleLikeNum(@PathVariable("slug") String slug) {
        Long likeNum = articleService.addArticleLikeNum(slug);
        return AjaxResult.success(likeNum);
    }


}
