package com.blog.business.utils;

import com.blog.common.utils.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 热度评分工具类
 *
 * @author 31373
 */
public class HotScoreUtils {

    /**
     * 默认冷却系数（时间衰减因子）
     * 数值越大，衰减越快
     */
    private static final double DEFAULT_COOLING_FACTOR = 0.1;

    /**
     * 回数权重系数
     */
    private static final double REPLY_COUNT_WEIGHT = 1.5;

    /**
     * 基础热度分数
     */
    private static final double BASE_SCORE = 100.0;

    /**
     * 计算留言热度分数
     * score = (基础分数 +回复数 *权系数) * e^(-α * t)
     *
     * @param createTime 创建时间
     * @param replyCount 回数量
     * @return 热度分数
     */
    public static double calculateHotScore(Date createTime, int replyCount) {
        return calculateHotScore(createTime, replyCount, DEFAULT_COOLING_FACTOR);
    }

    /**
     * 计算留言热度分数（自定义冷却系数）
     *
     * @param createTime    创建时间
     * @param replyCount    回复数量
     * @param coolingFactor 冷系数
     * @return 热度分数
     */
    public static double calculateHotScore(Date createTime, int replyCount, double coolingFactor) {
        if (createTime == null) {
            return 0.0;
        }

        // 计算时间差（小时）
        long timeDiffHours = calculateTimeDifferenceInHours(createTime);

        // 计算基础热度分数
        double baseHotScore = BASE_SCORE + (replyCount * REPLY_COUNT_WEIGHT);

        //应用时间衰减因子（指数衰减）
        double timeDecay = Math.exp(-coolingFactor * timeDiffHours);

        // 最终热度分数
        double finalScore = baseHotScore * timeDecay;
        //确分数不为负数
        return Math.max(0, finalScore);
    }

    /**
     * 计算时间差（小时）
     *
     * @param createTime 创建时间
     * @return 时间差（小时）
     */
    private static long calculateTimeDifferenceInHours(Date createTime) {
        Date now = new Date();
        long diffMillis = now.getTime() - createTime.getTime();
        return TimeUnit.MILLISECONDS.toHours(diffMillis);
    }

    /**
     * 计算留言热度等级
     *
     * @param hotScore 热分数
     * @return 热度等级（1-5级）
     */
    public static int calculateHotLevel(double hotScore) {
        if (hotScore >= 1000) {
            // 热
            return 5;
        } else if (hotScore >= 500) {
            // 较热
            return 4;
        } else if (hotScore >= 200) {
            // 较热
            return 3;
        } else if (hotScore >= 100) {
            // 一般
            return 2;
        } else {
            //较冷
            return 1;
        }
    }

    /**
     * 格化热度显示
     *
     * @param hotScore 热分数
     * @return 格式化后的热度显示
     */
    public static String formatHotScore(double hotScore) {
        if (hotScore >= 10000) {
            return String.format("%.1f万", hotScore / 10000);
        } else if (hotScore >= 1000) {
            return String.format("%.1f千", hotScore / 1000);
        } else {
            return String.format("%.0f", hotScore);
        }
    }

}
