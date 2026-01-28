package com.blog.common.filter;

import com.blog.common.constant.Constants;
import com.blog.common.utils.http.HttpHelper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 构建可重复读取inputStream的request
 *
 * @author 31373
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RepeatedlyRequestWrapper(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding(Constants.UTF8);
        response.setCharacterEncoding(Constants.UTF8);
        // 读取inputStream
        body = HttpHelper.getBodyString(request).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 获取request body
     *
     * @return request body
     * @throws IOException if an I/O error occurs
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 创建一个可重复读取的inputStream
        final ByteArrayInputStream byteBufInputStream = new ByteArrayInputStream(body);
        // 重写getInputStream方法
        return new ServletInputStream() {

            /**
             * 读取inputStream
             * @return int
             * @throws IOException if an I/O error occurs
             */
            @Override
            public int read() throws IOException {
                return byteBufInputStream.read();
            }

            /**
             * 获取inputStream中剩余的字节数
             * @return int
             * @throws IOException if an I/O error occurs
             */
            @Override
            public int available() throws IOException {
                return body.length;
            }

            /**
             * 判断inputStream是否已结束
             * @return boolean
             */
            @Override
            public boolean isFinished() {
                return false;
            }

            /**
             * 判断inputStream是否可读
             * @return boolean
             */
            @Override
            public boolean isReady() {
                return false;
            }

            /**
             * 设置读监听器
             * @param readListener 读监听器
             */
            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
