package com.blog.common.core.domain.entity;

import com.blog.common.core.domain.BaseEntity;

/**
 * 关于我
 * @author 31373
 */
public class AboutMe extends BaseEntity {

    private String aboutMeId;

    private String aboutMeContent;

    public String getAboutMeId() {
        return aboutMeId;
    }

    public void setAboutMeId(String aboutMeId) {
        this.aboutMeId = aboutMeId;
    }

    public String getAboutMeContent() {
        return aboutMeContent;
    }

    public void setAboutMeContent(String aboutMeContent) {
        this.aboutMeContent = aboutMeContent;
    }
}
