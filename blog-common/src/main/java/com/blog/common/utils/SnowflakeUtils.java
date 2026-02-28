package com.blog.common.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 雪花算法工具类
 * @author 31373
 */
public class SnowflakeUtils {

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake();

    /**
     * 生成雪花ID
     * @return 长整型ID
     */
    public static long generateId() {
        return SNOWFLAKE.nextId();
    }

    /**
     * 生成字符串格式的雪花ID
     * @return 字符串ID
     */
    public static String generateIdStr() {
        return SNOWFLAKE.nextIdStr();
    }

}
