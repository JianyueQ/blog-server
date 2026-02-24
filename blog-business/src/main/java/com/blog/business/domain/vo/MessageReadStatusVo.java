package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息阅读状态视图对象
 * @author 31373
 */
public class MessageReadStatusVo {

    private Long messageId;

    private Integer isRead;

    private String readTime;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("messageId", getMessageId())
                .append("isRead", getIsRead())
                .append("readTime", getReadTime())
                .toString();
    }

}
