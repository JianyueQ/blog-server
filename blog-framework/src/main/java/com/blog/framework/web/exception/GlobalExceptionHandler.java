package com.blog.framework.web.exception;

import com.blog.common.domain.AjaxResult;
import com.blog.common.exception.ServiceException;
import com.blog.common.text.Convert;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.html.EscapeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

/**
 * @author 31373
 */
@RestControllerAdvice // 全局异常处理
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     *
     * @param e 业务异常
     * @return 异常信息
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        if (StringUtils.isNotNull(code)) {
            return AjaxResult.error(code, e.getMessage());
        }
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", uri, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 拦截系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", uri, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                          HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value)) {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * 拦截上传文件异常
     */
    @ExceptionHandler(MultipartException.class)
    public AjaxResult handleMultipartException(MultipartException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',上传文件异常.", requestURI, e);
        return AjaxResult.error("上传文件异常，请检查文件大小和格式,以及网络连接");
    }

    /**
     * 拦截数据库唯一性异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public AjaxResult handleDuplicateKeyException(DuplicateKeyException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生数据库唯一性异常:{}", requestURI, e.getMessage());

        String message = e.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            // 提取 Duplicate entry '123123' for key 'articles.slug' 中的关键信息
            String[] parts = message.split("for key");
            if (parts.length > 0) {
                // 提取重复的值
                String valuePart = parts[0];
                int startIndex = valuePart.indexOf("'");
                int endIndex = valuePart.lastIndexOf("'");
                if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                    String duplicateValue = valuePart.substring(startIndex + 1, endIndex);
                    // 根据字段名返回友好的错误消息
                    return AjaxResult.error("'" + duplicateValue + "' 已存在");
                }
            }
        }
        return AjaxResult.error("数据违反唯一性约束：" + message);
    }

}
