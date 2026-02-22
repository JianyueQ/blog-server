package com.blog.business.domain.entity;

import com.blog.business.domain.vo.GuestbookListVo;
import com.blog.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 留言板
 * @author 31373
 */
public class Guestbook extends BaseEntity {
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
     * 状态：0-隐藏，1-显示,2-审核中
     */
    private Integer status;

    private List<Guestbook> replyList;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Guestbook> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Guestbook> replyList) {
        this.replyList = replyList;
    }
}
