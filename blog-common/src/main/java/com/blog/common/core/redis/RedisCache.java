package com.blog.common.core.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存
 *
 * @author 31373
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {

    private final RedisTemplate redisTemplate;

    public RedisCache(@Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 删除结果
     */
    public boolean deleteObject(final Collection collection) {
        return redisTemplate.delete(collection) > 0;
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperation.add(t);
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key     缓存键值
     * @param dataMap 待缓存的数据
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey) > 0;
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 缓存不可重复的值
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean setCacheUniqueValue(final String key, final String value, final long timeout, final TimeUnit unit) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        return result != null && result;
    }

    /**
     * 缓存元素到有序集合中
     *
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param score   分数
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean setCacheZSetValue(final String key, final Object value, final double score, final long timeout, final TimeUnit unit) {
        Boolean result = setCacheZSetValue(key, value, score);
        if (result != null && result) {
            expire(key, timeout, unit);
        }
        return result != null && result;
    }

    /**
     * 缓存元素到有序集合中
     * @param key 缓存的键值
     * @param newValue 新元素值
     * @param newScore 新分数
     * @return 是否更新成功
     */
    public Boolean setCacheZSetValue(final String key, final Object newValue, final double newScore) {
        return redisTemplate.opsForZSet().add(key, newValue, newScore);
    }

    /**
     * 按照分数获取有序集合中元素
     * @param key 缓存的键值
     * @param start 开始位置
     * @param end   结束位置
     * @return 有序集合
     * @param <T> 对象类型
     */
    public <T> Set<T> getCacheZSetValue(final String key, final long start, final long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取有序集合中所有的元素（倒序）
     * @param key 缓存的键值
     * @param start 开始位置
     * @param end 结束位置
     * @return 有序集合
     * @param <T> 对象类型
     */
    public <T> Set<T> getCacheZSetReverseValue(final String key, final long start, final long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取有序集合中所有的元素
     * @param key 缓存的键值
     * @return 有序集合
     * @param <T> 对象类型
     */
    public <T> Set<T> getCacheZSetValue(final String key) {
        return getCacheZSetValue(key, 0, -1);
    }

    /**
     * 获取有序集合中所有的元素（倒序）
     * @param key 缓存的键值
     * @return 有序集合
     * @param <T> 对象类型
     */
    public <T> Set<T> getCacheZSetReverseValue(final String key) {
        return getCacheZSetReverseValue(key, 0, -1);
    }

    /**
     * 删除有序集合中的某个元素
     * @param key   缓存的键值
     * @param value 缓存的元素值
     * @return 删除结果
     */
    public <T> Long deleteCacheZSetValue(final String key, final T value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }

    /**
     * 根据分数范围删除有序集合中的元素
     *
     * @param key      缓存的键值
     * @param minScore 最小分数
     * @param maxScore 最大分数
     */
    public void deleteZSetRangeByScore(final String key, final double minScore, final double maxScore) {
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }

    /**
     * 根据分数精确删除指定元素
     *
     * @param key   缓存的键值
     * @param score 精确分数
     */
    public void deleteZSetByScore(final String key, final double score) {
        deleteZSetRangeByScore(key, score, score);
    }

    /**
     * 递增字符串数字值
     * @param key Redis键
     * @param delta 递增幅度
     * @return 递增后的值
     */
    public Long increment(final String key, final long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递增字符串数字值（默认递增1）
     *
     * @param key Redis键
     */
    public Long increment(final String key) {
        return increment(key, 1);
    }

    /**
     * 递增字符串数字值（带过期时间）
     * @param key Redis键
     * @param delta 递增幅度
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @return 递增后的值
     */
    public Long increment(final String key, final long delta, final long timeout, final TimeUnit timeUnit) {
        Long result = increment(key, delta);
        if (result != null) {
            expire(key, timeout, timeUnit);
        }
        return result;
    }

    /**
     * 递增字符串数字值（默认递增1）（带过期时间）
     * @param key Redis键
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @return 递增后的值
     */
    public Long increment(final String key,final long timeout, final TimeUnit timeUnit) {
        Long result = increment(key);
        if (result != null) {
            expire(key, timeout, timeUnit);
        }
        return result;
    }

    /**
     * 递增Hash字段的数值
     * @param key Redis键
     * @param hashKey Hash字段键
     * @param delta 递增幅度
     * @return 递增后的值
     */
    public Long hashIncrement(final String key, final String hashKey, final long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 递增Hash字段的数值（默认递增1）
     * @param key Redis键
     * @param hashKey Hash字段键
     * @return 递增后的值
     */
    public Long hashIncrement(final String key, final String hashKey) {
        return hashIncrement(key, hashKey, 1);
    }

    /**
     * 递增Hash字段的数值（带过期时间）
     * @param key Redis键
     * @param hashKey Hash字段键
     * @param delta 递增幅度
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @return 递增后的值
     */
    public Long hashIncrement(final String key, final String hashKey, final long delta, final long timeout, final TimeUnit timeUnit) {
        Long result = hashIncrement(key, hashKey, delta);
        if (result != null) {
            expire(key, timeout, timeUnit);
        }
        return result;
    }

    /**
     * 当key存在时递增字符串数字值
     * @param key Redis键
     * @param delta 递增幅度
     * @return 递增后的值，如果key不存在则返回null
     */
    public Long incrementIfPresent(final String key, final long delta) {
        if (!hasKey(key)) {
            return null;
        }
        return increment(key, delta);
    }

    /**
     * 当key存在时递增字符串数字值（默认递增1）
     * @param key Redis键
     * @return 递增后的值，如果key不存在则返回null
     */
    public Long incrementIfPresent(final String key) {
        return incrementIfPresent(key, 1);
    }

    /**
     * 当Hash字段存在时递增数值
     * @param key Redis键
     * @param hashKey Hash字段键
     * @param delta 递增幅度
     * @return 递增后的值，如果Hash字段不存在则返回null
     */
    public Long hashIncrementIfPresent(final String key, final String hashKey, final long delta) {
        if (!redisTemplate.opsForHash().hasKey(key, hashKey)) {
            return null;
        }
        return hashIncrement(key, hashKey, delta);
    }

    /**
     * 当Hash字段存在时递增数值（默认递增1）
     * @param key Redis键
     * @param hashKey Hash字段键
     * @return 递增后的值，如果Hash字段不存在则返回null
     */
    public Long hashIncrementIfPresent(final String key, final String hashKey) {
        return hashIncrementIfPresent(key, hashKey, 1);
    }

    /**
     * 递减字符串数字值
     * @param key Redis键
     * @param delta 递减幅度
     * @return 递减后的值
     */
    public Long decrement(final String key, final long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
    /**
     * 递减字符串数字值（默认递减1）
     * @param key Redis键
     * @return 递减后的值
     */
    public Long decrement(final String key) {
        return decrement(key, 1);
    }

    /**
     * 递减Hash字段的数值
     * @param key Redis键
     * @param hashKey Hash字段键
     * @param delta 递减幅度
     * @return 递减后的值
     */
    public Long hashDecrement(final String key, final String hashKey, final long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }
    /**
     * 递减Hash字段的数值（默认递减1）
     * @param key Redis键
     * @param hashKey Hash字段键
     * @return 递减后的值
     */
    public Long hashDecrement(final String key, final String hashKey) {
        return hashDecrement(key, hashKey, 1);
    }

    /**
     * 递减Hash字段的数值（带过期时间）
     * @param key Redis键
     * @param hashKey Hash字段键
     * @param delta 递减幅度
     * @param timeout 过期时间
     * @param timeUnit 时间单位
     * @return 递减后的值
     */
    public Long hashDecrement(final String key, final String hashKey, final long delta, final long timeout, final TimeUnit timeUnit) {
        Long result = hashDecrement(key, hashKey, delta);
        if (result != null) {
            expire(key, timeout, timeUnit);
        }
        return result;
    }

    /**
     * 当Hash字段存在时递减数值
     * @param key Redis键
     * @param hashKey Hash字段键
     * @param delta 递减幅度
     * @return 递减后的值，如果Hash字段不存在则返回null
     */
    public Long hashDecrementIfPresent(final String key, final String hashKey, final long delta) {
        if (!redisTemplate.opsForHash().hasKey(key, hashKey)) {
            return null;
        }
        return hashDecrement(key, hashKey, delta);
    }

    /**
     * 当Hash字段存在时递减数值（默认递减1）
     * @param key Redis键
     * @param hashKey Hash字段键
     * @return 递减后的值，如果Hash字段不存在则返回null
     */
    public Long hashDecrementIfPresent(final String key, final String hashKey) {
        return hashDecrementIfPresent(key, hashKey, 1);
    }
}
