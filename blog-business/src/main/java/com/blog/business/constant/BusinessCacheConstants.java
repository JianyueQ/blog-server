package com.blog.business.constant;

/**
 * 业务缓存常量
 * @author 31373
 */
public class BusinessCacheConstants {

    /**
     * 缓存过期时间 1
     */
    public static final int CACHE_EXPIRE_TIME_ONE = 1;

    /**
     * 缓存过期时间 2
     */
    public static final int CACHE_EXPIRE_TIME_TWO = 2;

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

    /**
     * 后台根留言表根缓存
     */
    public static final String CACHE_GUESTBOOK_ROOT_LIST_KEY = "guestbook_list_cache:root";
    /**
     * 后台根留言表总数缓存
     */
    public static final String CACHE_GUESTBOOK_LIST_COUNT_KEY = "guestbook_list_cache:count";
    /**
     * 后台子留言列表缓存
     */
    public static final String CACHE_GUESTBOOK_CHILD_LIST_KEY = "guestbook_list_cache:child:";
    /**
     * 后台子留言总数缓存
     */
    public static final String CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY = "guestbook_list_cache:child_count";

    /**
     * 前台根留言索引缓存 guestbook:index:front_root: + rootId
     */
    public static final String FRONT_CACHE_GUESTBOOK_ROOT_INDEX_KEY = "guestbook:index:front_root:";
    /**
     * 前台根留言列表缓存 guestbook:list:front_root
     */
    public static final String FRONT_CACHE_GUESTBOOK_ROOT_LIST_KEY = "guestbook:list:front_root";
    /**
     * 前台留言总数缓存 guestbook:count:front_total
     */
    public static final String FRONT_CACHE_GUESTBOOK_LIST_COUNT_KEY = "guestbook:count:front_total";
    /**
     * 前台子留言索引缓存 guestbook:index:front_child: + rootId
     */
    public static final String FRONT_CACHE_GUESTBOOK_CHILD_INDEX_KEY = "guestbook:index:front_child:";
    /**
     * 前台子留言列表缓存 guestbook:list:front_child
     */
    public static final String FRONT_CACHE_GUESTBOOK_CHILD_LIST_KEY = "guestbook:list:front_child";
    /**
     * 前台子留言总数缓存 guestbook:count:front_child_total
     */
    public static final String FRONT_CACHE_GUESTBOOK_CHILD_LIST_COUNT_KEY = "guestbook:count:front_child_total";
    /**
     * 前台根留言总数缓存 guestbook:count:front_root_total
     */
    public static final String FRONT_CACHE_GUESTBOOK_ROOT_LIST_COUNT_KEY = "guestbook:count:front_root_total";

}
