package com.blog.common.utils;

/**
 * 日志工具类
 *
 * @author 31373
 */
public class LogUtils {

    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

}
