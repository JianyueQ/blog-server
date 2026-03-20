package com.blog.business.service.serviceImpl;

import com.blog.business.mapper.ArticlesCommentMapper;
import com.blog.business.service.ArticlesCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 31373
 */
@Service
public class ArticlesCommentServiceImpl implements ArticlesCommentService {

    @Autowired
    private ArticlesCommentMapper articlesCommentMapper;


}
