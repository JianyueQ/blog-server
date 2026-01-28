package com.blog.framework.interceptor;

import com.alibaba.fastjson2.JSON;
import com.blog.common.annotation.RepeatSubmit;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * 重复提交拦截器
 *
 * @author 31373
 */
@Component
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // HandlerMethod类型才代表具体的Controller方法，才能获取到注解信息
        if (handler instanceof HandlerMethod) {
            // 获取方法上的注解
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null){
                // 处理重复提交逻辑
                if (this.isRepeatSubmit(request,annotation)){
                    AjaxResult error = AjaxResult.error(annotation.message());
                    ServletUtils.renderString(response, JSON.toJSONString(error));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否重复提交
     * @param request 请求信息
     * @param repeatSubmit 防重复注解参数
     * @return true：重复提交 false：非重复提交
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request,RepeatSubmit repeatSubmit);
}
