package com.blog.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 31373
 */
@Configuration
@EnableCaching //设置缓存拦截器和相关的基础设施组件
public class RedisConfig extends CachingConfigurationSelector {

    /**
     * CachingConfigurerSupport 是 Spring 提供的一个抽象类，实现了 CachingConfigurer 接口
     * 提供默认实现：为缓存配置接口提供默认的空实现
     * 允许自定义配置：可以选择性地重写特定方法来自定义缓存行为
     * 简化配置：不需要实现接口的所有方法，只需重写需要自定义的部分
     */

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // 使用FastJson2JsonRedisSerializer替换StringRedisSerializer
        FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        log.info("redisTemplate 初始化完成");
        return redisTemplate;
    }

    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        log.info("DefaultRedisScript 初始化完成");
        return redisScript;
    }

    /**
     * 限流脚本: 计数限流算法
     */
    private String limitScriptText() {
        return "local key = KEYS[1] \n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";
    }
}
