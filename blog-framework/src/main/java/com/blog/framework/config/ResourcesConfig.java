package com.blog.framework.config;

import com.blog.framework.interceptor.RepeatSubmitInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

}
