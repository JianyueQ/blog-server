package com.blog.business.domain.dto;

/**
 * 消息阅读状态
 * @author 31373
 */
public class MessageReadStatusDto {

    private Long messageId;

    private Integer isRead;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
