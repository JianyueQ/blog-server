package com.blog.business.cacheKeyGenerator;

import com.blog.common.core.page.PageDomain;
import com.blog.common.core.page.TableSupport;
import com.blog.common.utils.StringUtils;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.StringJoiner;

/**
 * 通用缓存键生成器
 * 支持根据不同的DTO类型生成对应的缓存键
 * 自动去除缓存键中的所有空白字符（空格、制表符、换行符等）
 *
 * @author 31373
 */
@Component("CacheKeyGenerator")
public class CacheKeyGenerator implements KeyGenerator {

    private static final Logger log = LoggerFactory.getLogger(CacheKeyGenerator.class);

    /**
     * 下划线
     */
    private static final String UNDERSCORE = "_";

    /**
     * 空参数时的默认缓存键
     */
    private static final String DEFAULT_KEY = "all";

    /**
     * null 参数的缓存键
     */
    private static final String NULL_KEY = "null";

    /**
     * 最大字段深度，防止嵌套对象无限递归
     */
    private static final int MAX_DEPTH = 3;

    private static final String PUNCTUATION = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    /**
     * 生成缓存键
     *
     * @param target 目标对象
     * @param method 目标方法
     * @param params 方法参数
     * @return 缓存键
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        // 使用 StringJoiner 动态拼接缓存键
        StringJoiner keyJoiner = new StringJoiner(UNDERSCORE);
        // 添加分页参数到缓存键
        String paginationKey = extractPaginationParams();
        if (StringUtils.isNotEmpty(paginationKey)) {
            keyJoiner.add(paginationKey);
        }
        if (keyJoiner.length() != 0) {
            for (Object param : params) {
                // 提取参数的缓存键
                String paramKey = extractParamKey(param, 0);
                // 如果参数缓存键不为空，则添加到缓存键中
                if (StringUtils.isNotEmpty(paramKey)) {
                    keyJoiner.add(paramKey);
                }
            }
        }else {
            log.debug("生成缓存键: {} (无参数)", DEFAULT_KEY);
            return DEFAULT_KEY;
        }
        String cacheKey = keyJoiner.toString();
        log.debug("生成缓存键: {}", cacheKey);
        return cacheKey;
    }

    /**
     * 提取参数的缓存键
     *
     * @param param 参数
     * @param depth 当前递归深度
     * @return 缓存键
     */
    private String extractParamKey(Object param, int depth) {
        if (param == null) {
            return NULL_KEY;
        }
        if (depth >= MAX_DEPTH) {
            return String.valueOf(param.hashCode());
        }

        // 基本类型直接转字符串
        if (param instanceof String || param instanceof Number || param instanceof Boolean) {
            return cleanWhitespace(param.toString());
        }

        // 复杂对象通过反射提取
        return extractObjectKey(param, depth);
    }

    /**
     * 通过反射提取普通对象的缓存键
     *
     * @param obj   对象参数
     * @param depth 当前递归深度
     * @return 缓存键
     */
    private String extractObjectKey(Object obj, int depth) {
        StringJoiner joiner = new StringJoiner(UNDERSCORE);

        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                // 递归处理嵌套对象，增加深度
                String fieldValue = extractParamKey(value, depth + 1);
                joiner.add(fieldValue);
            }
        });

        return joiner.toString();
    }

    /**
     * 清理字符串中的空白字符和特殊符号
     *
     * @param str 输入字符串
     * @return 清理后的字符串
     */
    private String cleanWhitespace(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            // 获取当前字符
            char c = str.charAt(i);
            // 替换空白字符和标点符号为分隔符
            if (Character.isWhitespace(c) || Character.isISOControl(c) || isPunctuation(c)) {
                // 避免连续多个分隔符
                if (!sb.isEmpty() && sb.charAt(sb.length() - 1) != UNDERSCORE.charAt(0)) {
                    sb.append(UNDERSCORE);
                }
            } else {
                sb.append(c);
            }
        }

        // 移除末尾的分隔符
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == UNDERSCORE.charAt(0)) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
     * 判断字符是否为标点符号
     */
    private boolean isPunctuation(char c) {
        return PUNCTUATION.indexOf(c) >= 0;
    }

    /**
     * 提取分页参数作为缓存键的一部分
     */
    private String extractPaginationParams() {
        try {
            // 获取当前请求的分页参数
            PageDomain pageDomain = TableSupport.buildPageRequest();

            // 添加 null 检查
            if (pageDomain == null) {
                return "";
            }

            StringJoiner paginationJoiner = new StringJoiner("_");

            // 添加页码和每页大小
            if (StringUtils.isNotNull(pageDomain.getPageNum())) {
                paginationJoiner.add(String.valueOf(pageDomain.getPageNum()));
            }
            if (StringUtils.isNotNull(pageDomain.getPageSize())) {
                paginationJoiner.add(String.valueOf(pageDomain.getPageSize()));
            }

            // 添加排序信息
            String orderByColumn = pageDomain.getOrderByColumn();
            if (StringUtils.isNotEmpty(orderByColumn)) {
                paginationJoiner.add(orderByColumn);
            }

            String isAsc = pageDomain.getIsAsc();
            if (StringUtils.isNotEmpty(isAsc)) {
                paginationJoiner.add(isAsc);
            }

            return paginationJoiner.toString();
        } catch (Exception e) {
            // 如果获取分页参数失败，返回空字符串，不影响正常功能
            log.warn("获取分页参数失败: {}", e.getMessage());
            return "";
        }
    }
}