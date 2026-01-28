package com.blog.framework.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis使用FastJson进行序列化
 *
 * @author 31373
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    // 默认编码方式
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    //允许解析的包名
    public static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("com.blog");


    private Class<T> clazz;

    /**
     * 构造函数
     */
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        // 调用父类Object的构造函数
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化
     *
     * @param value 待序列化对象
     * @throws SerializationException 序列化异常
     */
    @Override
    public byte[] serialize(T value) throws SerializationException {
        if (value == null) {
            return new byte[0];
        }
        return JSON.toJSONString(value, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化
     *
     * @param bytes 待反序列化字节数组
     * @return 反序列化对象
     * @throws SerializationException 反序列化异常
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        // 字节数组转字符串
        String str = new String(bytes, DEFAULT_CHARSET);
        // 字符串转对象
        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }

}
