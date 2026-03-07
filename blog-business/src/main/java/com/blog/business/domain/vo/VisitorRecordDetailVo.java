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
     * 访客信息id
     */
    private String visitorInfoId;
    /**
     * 访客指纹
     */
    private String fingerprint;

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
     * 是否为黑名单（Y是 N否）
     */
    private String blacklist;

    /**
     * 访问次数
     */
    private Integer totalViews;
    /**
     *
     */
    private VisitorInfoVo visitorInfoVo;
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

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public String getVisitorInfoId() {
        return visitorInfoId;
    }

    public void setVisitorInfoId(String visitorInfoId) {
        this.visitorInfoId = visitorInfoId;
    }

    public VisitorInfoVo getVisitorInfoVo() {
        return visitorInfoVo;
    }

    public void setVisitorInfoVo(VisitorInfoVo visitorInfoVo) {
        this.visitorInfoVo = visitorInfoVo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("visitorRecordId", getVisitorRecordId())
                .append("visitorInfoId", getVisitorInfoId())
                .append("fingerprint", getFingerprint())
                .append("ipaddr", getIpaddr())
                .append("location", getLocation())
                .append("browser", getBrowser())
                .append("os", getOs())
                .append("visitTime", getVisitTime())
                .append("userAgent", getUserAgent())
                .append("blacklist", getBlacklist())
                .append("totalViews", getTotalViews())
                .append("visitorInfoVo", getVisitorInfoVo())
                .append("reason", getReason())
                .toString();
    }
}
