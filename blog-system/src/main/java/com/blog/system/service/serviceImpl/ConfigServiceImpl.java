package com.blog.system.service.serviceImpl;

import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.UserConstants;
import com.blog.common.core.domain.entity.SysConfig;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.exception.ServiceException;
import com.blog.common.text.Convert;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.StringUtils;
import com.blog.system.domain.dto.SysConfigDto;
import com.blog.system.domain.vo.SysConfigVo;
import com.blog.system.mapper.ConfigMapper;
import com.blog.system.service.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配置服务层实现
 *
 * @author 31373
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigMapper configMapper;
    private final RedisCache redisCache;

    public ConfigServiceImpl(ConfigMapper configMapper, RedisCache redisCache) {
        this.configMapper = configMapper;
        this.redisCache = redisCache;
    }

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }


    @Override
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }


    @Override
    public List<SysConfigVo> getSysConfig() {
        return configMapper.getSysConfig();
    }

    @Override
    public int updateSysConfig(SysConfigDto sysConfigDto) {
        SysConfig temp = configMapper.selectConfigById(sysConfigDto.getConfigId());
        if (!StringUtils.equals(temp.getConfigKey(), sysConfigDto.getConfigKey())) {
            redisCache.deleteObject(getCacheKey(temp.getConfigKey()));
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigId(sysConfigDto.getConfigId());
        sysConfig.setConfigName(sysConfigDto.getConfigName());
        sysConfig.setConfigKey(sysConfigDto.getConfigKey());
        sysConfig.setConfigValue(sysConfigDto.getConfigValue());
        sysConfig.setRemark(sysConfigDto.getRemark());
        sysConfig.setCreateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
        int row = configMapper.updateSysConfig(sysConfig);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(sysConfigDto.getConfigKey()), sysConfigDto.getConfigValue());
        }
        return row;
    }

    @Override
    public int insertConfig(SysConfigDto sysConfigDto) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigName(sysConfigDto.getConfigName());
        sysConfig.setConfigKey(sysConfigDto.getConfigKey());
        sysConfig.setConfigValue(sysConfigDto.getConfigValue());
        sysConfig.setRemark(sysConfigDto.getRemark());
        sysConfig.setCreateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
        sysConfig.setConfigType(UserConstants.NO);
        int row = configMapper.insertConfig(sysConfig);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(sysConfigDto.getConfigKey()), sysConfigDto.getConfigValue());
        }
        return row;
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config) {
        long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId() != configId) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = configMapper.selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteConfigById(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }


    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

}
