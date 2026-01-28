package com.blog.framework.config;

import com.blog.framework.interceptor.RepeatSubmitInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源配置类
 *
 * @author 31373
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ResourcesConfig.class);
    private final RepeatSubmitInterceptor repeatSubmitInterceptor;

    public ResourcesConfig(RepeatSubmitInterceptor repeatSubmitInterceptor) {
        this.repeatSubmitInterceptor = repeatSubmitInterceptor;
    }

    /**
     * 注册自拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        log.info("注册拦截器：{}", RepeatSubmitInterceptor.class.getName());
    }

}
