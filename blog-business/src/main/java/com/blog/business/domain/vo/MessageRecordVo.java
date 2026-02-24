package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录
 * @author 31373
 */
public class MessageRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long messageId;

    private String messageTitle;

    private String messageContent;

    private String messageType;

    private Integer isRead;

    private String readTime;

    private String createTime;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("messageId", getMessageId())
                .append("messageTitle", getMessageTitle())
                .append("messageContent", getMessageContent())
                .append("messageType", getMessageType())
                .append("isRead", getIsRead())
                .append("readTime", getReadTime())
                .append("createTime", getCreateTime())
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long messageId;
        private String messageTitle;
        private String messageContent;
        private String messageType;
        private Integer isRead;
        private String readTime;
        private String createTime;

        public Builder messageId(Long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder messageTitle(String messageTitle) {
            this.messageTitle = messageTitle;
            return this;
        }

        public Builder messageContent(String messageContent) {
            this.messageContent = messageContent;
            return this;
        }

        public Builder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder isRead(Integer isRead) {
            this.isRead = isRead;
            return this;
        }

        public Builder readTime(String readTime) {
            this.readTime = readTime;
            return this;
        }

        public Builder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public MessageRecordVo build() {
            MessageRecordVo vo = new MessageRecordVo();
            vo.setMessageId(this.messageId);
            vo.setMessageTitle(this.messageTitle);
            vo.setMessageContent(this.messageContent);
            vo.setMessageType(this.messageType);
            vo.setIsRead(this.isRead);
            vo.setReadTime(this.readTime);
            vo.setCreateTime(this.createTime);
            return vo;
        }
    }
}
