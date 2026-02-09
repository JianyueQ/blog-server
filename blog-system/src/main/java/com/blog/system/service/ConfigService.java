package com.blog.system.service;

import com.blog.common.core.domain.entity.SysConfig;
import com.blog.system.domain.dto.SysConfigDto;
import com.blog.system.domain.vo.SysConfigVo;

import java.util.List;

/**
 * 配置服务层
 * @author 31373
 */
public interface ConfigService {
    void loadingConfigCache();

    boolean selectCaptchaEnabled();

    String selectConfigByKey(String configKey);

    List<SysConfigVo> getSysConfig();

    int updateSysConfig(SysConfigDto sysConfigDto);

    public int insertConfig(SysConfigDto sysConfigDto);

    boolean checkConfigKeyUnique(SysConfig config);

    void deleteConfigByIds(Long[] configIds);
}
