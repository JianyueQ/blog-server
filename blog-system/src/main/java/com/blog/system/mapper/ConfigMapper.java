package com.blog.system.mapper;

import com.blog.common.core.domain.entity.SysConfig;
import com.blog.system.domain.vo.SysConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface ConfigMapper {

    SysConfig selectConfig(SysConfig config);

    List<SysConfigVo> getSysConfig();

    int updateSysConfig(SysConfig sysConfig);

    SysConfig selectConfigById(@Param("configId") Long configId);

    List<SysConfig> selectConfigList(SysConfig sysConfig);

    int insertConfig(SysConfig sysConfig);

    SysConfig checkConfigKeyUnique(String configKey);

    void deleteConfigById(Long configId);
}
