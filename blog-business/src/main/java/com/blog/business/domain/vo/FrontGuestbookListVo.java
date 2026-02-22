package com.blog.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author 31373
 */
public class FrontGuestbookListVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 留言id
     */
    private Long guestbookId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 留言内容
     */
    private String content;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * ip所在地区
     */
    private String location;
    /**
     * 根留言id，0表示根留言
     */
    private Long rootId;
    /**
     * 回复留言id，0表示直接回复根留言
     */
    private Long parentId;
    /**
     * 是否为根留言：0-否，1-是
     */
    private Integer isRoot;

    /**
     * 留言时间
     */
    private String messageTime;

    /**
     * 回复列表
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FrontGuestbookListVo> replyList;

    public Long getGuestbookId() {
        return guestbookId;
    }

    public void setGuestbookId(Long guestbookId) {
        this.guestbookId = guestbookId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot) {
        this.isRoot = isRoot;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public List<FrontGuestbookListVo> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<FrontGuestbookListVo> replyList) {
        this.replyList = replyList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("guestbookId", getGuestbookId())
                .append("nickname", getNickname())
                .append("email", getEmail())
                .append("content", getContent())
                .append("avatar", getAvatar())
                .append("location", getLocation())
                .append("rootId", getRootId())
                .append("parentId", getParentId())
                .append("isRoot", getIsRoot())
                .append("messageTime", getMessageTime())
                .append("replyList", getReplyList())
                .toString();
    }

}
