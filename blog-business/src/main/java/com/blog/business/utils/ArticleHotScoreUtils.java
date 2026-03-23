package com.blog.business.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
public class ArticleHotScoreUtils {

    /**
     * 浏览次数权重
     */
    private static final double VIEWS_WEIGHT = 1.0;
    /**
     * 点赞权重
     */
    private static final double LIKES_WEIGHT = 5.0;
    /**
     * 评论权重
     */
    private static final double COMMENTS_WEIGHT = 10.0;
    /**
     * 时间衰减因子
     */
    private static final double TIME_DECAY_BASE = 2.0;
    /**
     * 时间衰减引力
     */
    private static final double TIME_DECAY_GRAVITY = 1.8;

    /**
     * 计算文章的热度评分
     *
     * @param views       浏览次数
     * @param likes       点赞数
     * @param comments    评论数
     * @param publishTime 发布时间
     * @return 文章热度
     */
    public static double calculateHotScore(int views, int likes, int comments, Date publishTime) {
        return calculateHotScore(views, likes, comments, publishTime, VIEWS_WEIGHT, LIKES_WEIGHT, COMMENTS_WEIGHT, TIME_DECAY_BASE, TIME_DECAY_GRAVITY);
    }

    /**
     * @param views            浏览次数
     * @param likes            点赞数
     * @param comments         评论数
     * @param publishTime      发布时间
     * @param viewsWeight      浏览次数权重
     * @param likesWeight      点赞权重
     * @param commentsWeight   评论权重
     * @param timeDecayBase    时间衰减因子
     * @param timeDecayGravity 时间衰减引力
     * @return 文章热度
     */
    public static double calculateHotScore(int views, int likes, int comments, Date publishTime,
                                           double viewsWeight, double likesWeight, double commentsWeight,
                                           double timeDecayBase, double timeDecayGravity) {
        long hoursSincePublish = getHoursSince(publishTime);
        double rawScore = viewsWeight * views + likesWeight * likes + commentsWeight * comments;
        double decayFactor = Math.pow(hoursSincePublish + timeDecayBase, timeDecayGravity);
        return rawScore / decayFactor;
    }

    /**
     * 计算文章的热度评分
     *
     * @param views       浏览次数
     * @param likes       点赞数
     * @param comments    评论数
     * @param publishTime 发布时间
     * @return 文章热度
     */
    public static double calculateHotScoreWithExponentialDecay(int views, int likes, int comments, Date publishTime) {
        long hoursSincePublish = getHoursSince(publishTime);
        double rawScore = VIEWS_WEIGHT * views + LIKES_WEIGHT * likes + COMMENTS_WEIGHT * comments;
        double decay = Math.exp(-0.05 * hoursSincePublish);
        return rawScore * decay;
    }

    /**
     * 计算文章的热度评分
     *
     * @param publishTime 发布时间
     * @return 以小时为单位的时间差
     */
    private static long getHoursSince(Date publishTime) {
        if (publishTime == null) {
            return 0;
        }
        long diffMillis = System.currentTimeMillis() - publishTime.getTime();
        return TimeUnit.MILLISECONDS.toHours(diffMillis);
    }

    /**
     * 获取浏览次数权重
     */
    public static double getViewsWeight() {
        return VIEWS_WEIGHT;
    }

    /**
     * 获取点赞数权重
     */
    public static double getLikesWeight() {
        return LIKES_WEIGHT;
    }

    /**
     * 获取评论数权重
     */
    public static double getCommentsWeight() {
        return COMMENTS_WEIGHT;
    }

    /**
     * 获取时间衰减因子
     */
    public static double getTimeDecayBase() {
        return TIME_DECAY_BASE;
    }

    /**
     * 获取时间衰减引力
     */
    public static double getTimeDecayGravity() {
        return TIME_DECAY_GRAVITY;
    }
}
