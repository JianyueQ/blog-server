package com.blog.business.domain.dto;

/**
 * 访客记录
 *
 * @author 31373
 */
public class VisitorRecordDto {
    /**
     * 访客记录id
     */
    private Long visitorRecordId;

    /**
     * 拉黑原因
     */
    private String reason;

    /**
     * 是否拉黑 Y:是 N:否
     */
    private String isBlacklist;

    public Long getVisitorRecordId() {
        return visitorRecordId;
    }

    public void setVisitorRecordId(Long visitorRecordId) {
        this.visitorRecordId = visitorRecordId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(String isBlacklist) {
        this.isBlacklist = isBlacklist;
    }
}
