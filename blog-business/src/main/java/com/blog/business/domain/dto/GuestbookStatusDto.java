package com.blog.business.domain.dto;

/**
 * @author 31373
 */
public class GuestbookStatusDto {

    /**
     * 留言id
     */
    private Long guestbookId;

    /**
     * 状态：0-隐藏，1-显示,2-审核中
     */
    private Integer status;

    public Long getGuestbookId() {
        return guestbookId;
    }

    public void setGuestbookId(Long guestbookId) {
        this.guestbookId = guestbookId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
