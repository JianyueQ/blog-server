package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;

/**
 * @author 31373
 */
public class Announcement extends BaseEntity {
    private Long announcementId;
    private String content;

    public Long getAnnouncementId() {
        return announcementId;
    }

    public void setAnnouncementId(Long announcementId) {
        this.announcementId = announcementId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
