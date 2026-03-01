package com.blog.common.utils;

import com.blog.common.constant.CacheConstants;
import com.blog.common.core.redis.RedisCache;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *系统累计运行时长统计工具类
 *基于JVM启动时间计算累计运行时长
 *
 * @author 31373
 */
@Component
public class SystemUptimeUtils {

    private static final Logger log = LoggerFactory.getLogger(SystemUptimeUtils.class);

    @Autowired
    private RedisCache redisCache;

    // Redis中存储累计运行时长的key
    private static final String TOTAL_UPTIME_KEY = CacheConstants.SYS_CONFIG_KEY + "total_uptime";

    //累运行时长（毫秒）
    private static Long totalUptime = 0L;

    /**
     * 获取累计运行时长
     * @return 累计运行时长
     */
    public static String getTotalUptime() {
        //获取系统当前运行时长(毫秒)
        long runTime = getRunTime();
        //计算累计运行时长 历史运行时长 + 系统当前运行时长 (毫秒)
        long uptime = totalUptime + runTime;
        //将运行时长转换为字符串并返回
        return formatTime(uptime);
    }

    /**
     * JDK运行时间
     */
    public static long getRunTime() {
        return timeDistance(DateUtils.getNowDate(), DateUtils.getServerStartDate());
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static long timeDistance(Date endDate, Date startTime) {
        // 获得两个时间的毫秒时间差异
        return endDate.getTime() - startTime.getTime();
    }

    /**
     * 将毫秒转换成天/小时/分钟
     */
    public static String formatTime(long millis) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 计算差多少天
        long day = millis / nd;
        // 计算差多少小时
        long hour = millis % nd / nh;
        // 计算差多少分钟
        long min = millis % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 重置累计运行时长（谨慎使用）
     */
    public synchronized void resetTotalUptime() {
        totalUptime = 0L;
        redisCache.deleteObject(TOTAL_UPTIME_KEY);
        log.warn("累计运行时长已重置为0");
    }

    /**
     * 更新并保存累计运行时长到Redis
     * 通常在系统关闭时调用
     */
    public synchronized void updateAndSaveTotalUptime() {
        totalUptime = totalUptime + getRunTime();
        redisCache.setCacheObject(TOTAL_UPTIME_KEY, totalUptime);
    }

    /**
     * 应用启动时初始化
     * 从Redis恢复历史累计运行时长
     */
    @PostConstruct
    public void init() {
        //从redis中获取存储的累计运行时长
        Long savedUptime = redisCache.getCacheObject(TOTAL_UPTIME_KEY);
        if (StringUtils.isNotNull(savedUptime)) {
            totalUptime = savedUptime;
            log.debug("从Redis恢复历史累计运行时长: {}毫秒", totalUptime);
        } else {
            totalUptime = 0L;
            log.debug("Redis中无历史累计运行时长数据，初始化为0");
        }
    }
}