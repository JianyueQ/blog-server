package com.blog.common.utils;

import com.blog.common.constant.CacheConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.utils.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *系统累计运行时长统计工具类
 *基于JVM启动时间计算累计运行时长
 * 
 * @author 31373
 */
@Component
public class SystemUptimeUtils {
    
    @Autowired
    private RedisCache redisCache;
    
    // Redis中存储累计运行时长的key
    private static final String TOTAL_UPTIME_KEY = CacheConstants.SYS_CONFIG_KEY + "total_uptime";
    
    //应用启动时间
    private static Date applicationStartTime;
    
    //累运行时长（毫秒）
    private static Long totalUptime = 0L;
    
    /**
     *应用启动时初始化
     * 从Redis恢复历史累计运行时长
     */
    @PostConstruct
    public void init() {
        // 获取当前应用启动时间（基于JVM启动时间）
        applicationStartTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
        
        // 从Redis获取历史累计运行时长
        Object cachedTotalUptime = redisCache.getCacheObject(TOTAL_UPTIME_KEY);
        if (cachedTotalUptime != null) {
            try {
                if (cachedTotalUptime instanceof Long) {
                    totalUptime = (Long) cachedTotalUptime;
                } else if (cachedTotalUptime instanceof String) {
                    totalUptime = Long.parseLong((String) cachedTotalUptime);
                } else if (cachedTotalUptime instanceof Number) {
                    totalUptime = ((Number) cachedTotalUptime).longValue();
                }
            } catch (NumberFormatException e) {
                System.err.println("Redis中存储的累计运行时长数据格式错误: " + cachedTotalUptime);
                totalUptime = 0L;
            }
        }
        
        System.out.println("系统累计运行时长统计初始化完成");
        System.out.println("应用启动时间: " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, applicationStartTime));
    }
    
    /**
     * 获取当前会话运行时长（自本次启动以来）
     * 
     * @return 运行时间（毫秒）
     */
    public static long getCurrentSessionUptime() {
        if (applicationStartTime == null) {
            return 0L;
        }
        return System.currentTimeMillis() - applicationStartTime.getTime();
    }
    
    /**
     * 获取系统累计运行时长（包含历史运行时间）
     * 
     * @return 总运行时间（毫秒）
     */
    public static synchronized long getTotalUptime() {
        return totalUptime + getCurrentSessionUptime();
    }
    
    /**
     * 更新并保存累计运行时长到Redis
     * 通常在系统关闭时调用
     */
    public static synchronized void updateAndSaveTotalUptime() {
        long currentSessionUptime = getCurrentSessionUptime();
        totalUptime += currentSessionUptime;

        // 保存到Redis
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        if (redisCache != null) {
            redisCache.setCacheObject(TOTAL_UPTIME_KEY, totalUptime);
        }

        System.out.println("系统累计运行时长已保存: " + formatDuration(totalUptime));
    }
    
    /**
     *格化时间间隔
     * 
     * @param milliseconds 毫秒数
     * @return 格式化的时间字符串（如：1天2小时3分钟4秒）
     */
    public static String formatDuration(long milliseconds) {
        if (milliseconds <= 0) {
            return "0秒";
        }
        
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        
        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 24;
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (seconds > 0 || sb.length() == 0) {
            sb.append(seconds).append("秒");
        }
        
        return sb.toString();
    }
    
    /**
     * 获取系统启动时间
     * 
     * @return 系统启动时间
     */
    public static Date getApplicationStartTime() {
        return applicationStartTime;
    }
    
    /**
     * 重置累计运行时长（谨慎使用）
     */
    public static synchronized void resetTotalUptime() {
        totalUptime = 0L;
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        if (redisCache != null) {
            redisCache.deleteObject(TOTAL_UPTIME_KEY);
        }
        System.out.println("系统累计运行时长已重置");
    }
}