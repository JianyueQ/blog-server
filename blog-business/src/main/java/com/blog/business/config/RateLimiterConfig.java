package com.blog.business.config;

import com.blog.business.interceptor.VisitorRecordInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 31373
 */
@Configuration
public class RateLimiterConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterConfig.class);
    @Autowired
    private VisitorRecordInterceptor visitorRecordInterceptor;

    /**
     * 注册限流拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitorRecordInterceptor)
                .addPathPatterns("/blog/**");
        log.debug("注册限流拦截器：{}", VisitorRecordInterceptor.class.getName());
    }


}
