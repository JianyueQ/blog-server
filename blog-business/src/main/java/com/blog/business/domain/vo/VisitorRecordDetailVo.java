package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 访客记录详情
 * @author 31373
 */
public class VisitorRecordDetailVo {

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
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 访问时间
     */
    private String visitTime;

    /**
     * 用户代理字符串
     */
    private String userAgent;

    /**
     * 是否为异常访问(0:正常 1:异常)
     */
    private Integer abnormalFlag;

    /**
     * 异常类型
     */
    private String abnormalType;

    /**
     * blacklist
     */
    private String blacklist;

    /**
     * reason
     */
    private String reason;

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

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getAbnormalFlag() {
        return abnormalFlag;
    }

    public void setAbnormalFlag(Integer abnormalFlag) {
        this.abnormalFlag = abnormalFlag;
    }

    public String getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(String abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("visitorRecordId", getVisitorRecordId())
                .append("ipaddr", getIpaddr())
                .append("location", getLocation())
                .append("browser", getBrowser())
                .append("os", getOs())
                .append("visitTime", getVisitTime())
                .append("userAgent", getUserAgent())
                .append("abnormalFlag", getAbnormalFlag())
                .append("abnormalType", getAbnormalType())
                .append("blacklist", getBlacklist())
                .append("reason", getReason())
                .toString();
    }
}
