package com.blog.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author 31373
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String YYYY_MM_DD_HH_MM_SS_SSS_SSS = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取当前时间的毫秒级时间戳
     * 用于ZSet排序，保证时间精度不丢失
     * @return 毫秒级时间戳
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间的微秒级时间戳（纳秒/1000）
     * 提供更高精度的时间戳，适用于高并发场景
     * @return 微秒级时间戳
     */
    public static long getCurrentTimeMicros() {
        return System.nanoTime() / 1000;
    }

    /**
     * 获取当前时间的纳秒级时间戳
     * 最高精度，适用于极高并发场景
     * @return 纳秒级时间戳
     */
    public static long getCurrentTimeNanos() {
        return System.nanoTime();
    }

    /**
     * 将Date转换为毫秒级时间戳
     * 如果date为null，返回当前时间戳
     * @param date 日期对象
     * @return 毫秒级时间戳
     */
    public static long toTimeMillis(Date date) {
        if (date == null) {
            return getCurrentTimeMillis();
        }
        return date.getTime();
    }

    /**
     * 将毫秒级时间戳转换为Date对象
     * @param timestamp 毫秒级时间戳
     * @return Date对象
     */
    public static Date fromTimeMillis(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 获取当前时间的毫秒级字符串（包含毫秒）
     * 格式：yyyy-MM-dd HH:mm:ss.SSS
     * @return 毫秒级时间字符串
     */
    public static String getCurrentTimeWithMillis() {
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS_SSS, new Date());
    }

    /**
     * 获取当前时间的微秒级字符串
     * 格式：yyyy-MM-dd HH:mm:ss.SSSSSS
     * @return 微秒级时间字符串
     */
    public static String getCurrentTimeWithMicros() {
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS_SSS_SSS, new Date());
    }

    /**
     * 将时间戳转换为指定格式的字符串
     * @param timestamp 毫秒级时间戳
     * @param format 格式字符串
     * @return 格式化的时间字符串
     */
    public static String formatTimestamp(long timestamp, String format) {
        return parseDateToStr(format, new Date(timestamp));
    }

    /**
     * 生成适用于ZSet排序的分数（毫秒级时间戳 + 序列号）
     * 解决同一毫秒内多条数据排序问题
     * @param timestamp 基础时间戳
     * @param sequence 序列号（0-999）
     * @return ZSet排序分数
     */
    public static double generateZSetScore(long timestamp, int sequence) {
        if (sequence < 0 || sequence > 999) {
            sequence = sequence % 1000;
        }
        // 时间戳在前，序列号在后，保证时间排序
        return timestamp * 1000L + sequence;
    }

    /**
     * 生成适用于ZSet排序的分数（当前时间戳 + 随机序列号）
     * 自动处理序列号，适用于单机环境
     * @return ZSet排序分数
     */
    public static double generateZSetScore() {
        long timestamp = getCurrentTimeMillis();
        int sequence = (int)(System.nanoTime() % 1000);
        return generateZSetScore(timestamp, sequence);
    }

    /**
     * 从ZSet分数中提取时间戳
     * @param score ZSet分数
     * @return 毫秒级时间戳
     */
    public static long extractTimestampFromScore(double score) {
        return (long)(score / 1000);
    }
}
