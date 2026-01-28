package com.blog.common.utils.http;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 通用http工具封装
 *
 * @author 31373
 */
public class HttpHelper {


    private static final Logger log = LoggerFactory.getLogger(HttpHelper.class);

    /**
     * 获取请求的body字符串
     *
     * @param request 请求
     * @return body字符串
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        //创建一个BufferedReader去读取request中的数据
        BufferedReader reader = null;
        //确保资源释放
        try (ServletInputStream inputStream = request.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("getBodyString获取请求体字符串出现问题：", e);
        } finally {
            // 确保资源释放,防止内存泄漏或连接池耗尽
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        }
        return sb.toString();
    }

}
