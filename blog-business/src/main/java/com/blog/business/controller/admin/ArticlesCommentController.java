package com.blog.business.controller.admin;

import com.blog.business.service.ArticlesCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章评论
 * @author 31373
 */
@RestController
@RequestMapping("/system/articles/comment")
public class ArticlesCommentController {

    @Autowired
    private ArticlesCommentService articlesCommentService;

    /**
     * 获取文章评论
     */


}
