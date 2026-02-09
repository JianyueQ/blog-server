package com.blog.system.domain.dto;

/**
 * 系统配置
 * @author 31373
 */
public class SysConfigDto {

    /**
     * 参数id
     */
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数值
     */
    private String configValue;
    /**
     * 参数键
     */
    private String configKey;

    /**
     * 备注
     */
    private String remark;

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
