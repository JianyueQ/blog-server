package com.blog.common.core.domain.entity;

import com.blog.common.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 访问记录
 *
 * @author 31373
 */
public class VisitorRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录id
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
     * 异常类型(SCRIPT:脚本访问, SPAM:垃圾请求, RATE_LIMIT:频率超限等)
     */
    private String abnormalType;

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
}
