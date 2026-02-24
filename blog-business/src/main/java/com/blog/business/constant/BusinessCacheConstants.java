package com.blog.business.constant;

/**
 * 业务缓存常量
 * @author 31373
 */
public class BusinessCacheConstants {
    /**
     * 留言板列表缓存
     */
    public static final String GUESTBOOK_LIST_CACHE = "guestbook_list_cache";
    /**
     * 前台留言列表缓存
     */
    public static final String GUESTBOOK_LIST_FRONT_CACHE = "guestbook_list_cache:front";

    /**
     * 消息记录列表缓存
     */
    public static final String MESSAGE_RECORD_LIST_CACHE = "message_record_list_cache";
    /**
     * 消息记录列表未读缓存
     */
    public static final String MESSAGE_RECORD_LIST_UNREAD_CACHE = "message_record_list_cache:unread";
    /**
     * 消息记录列表已读缓存
     */
    public static final String MESSAGE_RECORD_LIST_READ_CACHE = "message_record_list_cache:read";
}
