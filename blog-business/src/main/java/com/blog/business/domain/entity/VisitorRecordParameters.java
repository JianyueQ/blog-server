package com.blog.business.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 访客记录参数
 * @author 31373
 */
public class VisitorRecordParameters {
    // 前端收集浏览器信息,用于制作访客指纹
    /**
     * 来源页面
     */
    private String referer;
    /**
     * 屏幕分辨率 "1920x1080"
     */
    private String screen;
    /**
     * 时区 "Asia/Shanghai"
     */
    private String timezone;
    /**
     *  语言 "zh-CN"
     */
    private String language;
    /**
     * 平台 "Win32"
     */
    private String platform;
    /**
     * 是否支持Cookie
     */
    private Boolean cookiesEnabled;
    /**
     * 设备内存
     */
    private Integer deviceMemory;
    /**
     * visitor
     */
    private VisitorInfo visitor;

    /**
     * CPU核心数
     */
    private Integer hardwareConcurrency;

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Boolean getCookiesEnabled() {
        return cookiesEnabled;
    }

    public void setCookiesEnabled(Boolean cookiesEnabled) {
        this.cookiesEnabled = cookiesEnabled;
    }

    public Integer getDeviceMemory() {
        return deviceMemory;
    }

    public void setDeviceMemory(Integer deviceMemory) {
        this.deviceMemory = deviceMemory;
    }

    public Integer getHardwareConcurrency() {
        return hardwareConcurrency;
    }

    public void setHardwareConcurrency(Integer hardwareConcurrency) {
        this.hardwareConcurrency = hardwareConcurrency;
    }

    public VisitorInfo getVisitor() {
        return visitor;
    }

    public void setVisitor(VisitorInfo visitor) {
        this.visitor = visitor;
    }
}
