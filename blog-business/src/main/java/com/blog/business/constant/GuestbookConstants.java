package com.blog.business.constant;

/**
 * 留言板常量
 *
 * @author 31373
 */
public class GuestbookConstants {
    /**
     * 是否是根留言 1是
     */
    public static final Integer IS_ROOT = 1;
    /**
     * 是否是根留言 0否
     */
    public static final Integer NOT_ROOT = 0;
    /**
     * 根留言ID 0表示根留言
     */
    public static final Long ROOT_ID = 0L;
    /**
     * 父留言ID 0表示直接回复根留言
     */
    public static final Long PARENT_ID = 0L;
}
