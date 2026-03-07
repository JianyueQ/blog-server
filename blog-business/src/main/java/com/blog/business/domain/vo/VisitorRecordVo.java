package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 访客记录
 * @author 31373
 */
public class VisitorRecordVo {
    /**
     * 访客记录id
     */
    private Long visitorRecordId;
    /**
     * ip地址
     */
    private String ipaddr;

    /**
     * ip所在地区
     */
    private String location;

    /**
     * 访问次数
     */
    private Integer totalViews;

    /**
     * 访问时间
     */
    private String visitTime;

    public Long getVisitorRecordId() {
        return visitorRecordId;
    }

    public void setVisitorRecordId(Long visitorRecordId) {
        this.visitorRecordId = visitorRecordId;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("visitorRecordId", getVisitorRecordId())
                .append("ipaddr", getIpaddr())
                .append("location", getLocation())
                .append("totalViews", getTotalViews())
                .append("visitTime", getVisitTime())
                .toString();
    }
}
