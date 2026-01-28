package com.blog.common.exception;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author 31373
 */
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误详细提示
     */
    private String detailMessage;

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public ServiceException setCode(Integer code) {
        this.code = code;
        return this;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {

    }

    /**
     * 带错误码的构造方法
     *
     * @param code    错误码
     * @param message 错误提示
     */
    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 不带错误码的构造方法
     * @param message 错误提示
     */
    public ServiceException(String message) {
        this.message = message;
    }
}
