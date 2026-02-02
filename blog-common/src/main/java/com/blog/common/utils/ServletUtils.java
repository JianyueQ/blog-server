package com.blog.common.utils;

import com.blog.common.constant.Constants;
import com.blog.common.text.Convert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

/**
 * @author 31373
 */
public class ServletUtils {


    private static final Logger log = LoggerFactory.getLogger(ServletUtils.class);

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setCharacterEncoding(Constants.UTF8);
            response.setContentType(Constants.CONTENT_TYPE_JSON);
            response.getWriter().print(string);
        } catch (IOException e) {
//            e.printStackTrace();
            log.error("响应字符串至客户端失败:", e);
        }
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static Boolean getParameterToBool(String name) {
        return Convert.toBool(getRequest().getParameter(name));
    }
}
