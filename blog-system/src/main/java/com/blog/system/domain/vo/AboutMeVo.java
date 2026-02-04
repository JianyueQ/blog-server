package com.blog.system.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author 31373
 */
public class AboutMeVo {

    private String aboutMeId;

    private String aboutMeContent;

    private String updateTime;

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("aboutMeId", getAboutMeId())
                .append("aboutMeContent", getAboutMeContent())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
