package com.blog.business.exception.artices;

import com.blog.common.exception.base.BaseException;

import java.io.Serial;

/**
 * 文章管理异常
 * @author 31373
 */
public class ArticlesException extends BaseException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ArticlesException(String code, Object[] args) {
        super("articles", code, args, null);
    }

    public ArticlesException(String message){
        super(message);
    }
}
