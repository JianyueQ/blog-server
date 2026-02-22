package com.blog.business.domain.dto;

import com.blog.common.core.domain.BaseEntity;
import com.blog.common.xss.Xss;

/**
 * 留言板
 * @author 31373
 */
public class GuestbookDto extends BaseEntity {
    /**
     * 昵称
     */
    @Xss
    private String nickname;
    /**
     * 邮箱
     */
    @Xss
    private String email;
    /**
     * 留言内容
     */
    @Xss
    private String content;
    /**
     * 头像地址
     */
    @Xss
    private String avatar;
    /**
     * 根留言id，0表示根留言
     */
    private Long rootId;
    /**
     * 回复留言id，0表示直接回复根留言
     */
    private Long parentId;

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
}
