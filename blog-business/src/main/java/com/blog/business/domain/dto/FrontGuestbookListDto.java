package com.blog.business.domain.dto;

/**
 * 前台留言列表条件
 *
 * @author 31373
 */
public class FrontGuestbookListDto{

    /**
     * 留言id
     */
    private Long guestbookId;
    /**
     * 是否为根留言：0-否，1-是
     */
    private Integer isRoot;

    public Long getGuestbookId() {
        return guestbookId;
    }

    public void setGuestbookId(Long guestbookId) {
        this.guestbookId = guestbookId;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot) {
        this.isRoot = isRoot;
    }
}
