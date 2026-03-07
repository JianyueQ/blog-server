package com.blog.business.domain.entity;

import com.blog.common.core.domain.BaseEntity;

/**
 * 访客记录
 * @author 31373
 */
public class VisitorRecord extends BaseEntity {
    /**
     * 访客记录id
     */
    private Long visitorRecordId;
    /**
     *    访客指纹,用于唯一标识访客
     */
    private String fingerprint;
    /**
     * 访客信息id
     */
    private Long visitorInfoId ;
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
     * 访问次数
     */
    private Integer totalViews;

    /**
     * 是否为黑名单（Y是 N否）
     */
    private String blacklist;

    /**
     * 加入黑名单的原因
     */
    private String reason;
    /**
     * 客户端数据
     */
    private String clientData;
    /**
     * 用户代理字符串
     */
    private String userAgent;

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

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Long getVisitorInfoId() {
        return visitorInfoId;
    }

    public void setVisitorInfoId(Long visitorInfoId) {
        this.visitorInfoId = visitorInfoId;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
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

    public String getClientData() {
        return clientData;
    }

    public void setClientData(String clientData) {
        this.clientData = clientData;
    }
}
