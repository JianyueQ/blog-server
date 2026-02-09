package com.blog.web.controller.system;

import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.entity.SysConfig;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import com.blog.system.domain.dto.SysConfigDto;
import com.blog.system.domain.vo.SysConfigVo;
import com.blog.system.service.ConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigurationController extends BaseController {

    private final ConfigService configService;

    public SysConfigurationController(ConfigService configService) {
        this.configService = configService;
    }


    /**
     * 查询配置列表
     */
    @GetMapping("/sysConfig")
    public AjaxResult getSysConfig() {
        List<SysConfigVo> sysConfig = configService.getSysConfig();
        return AjaxResult.success(sysConfig);
    }

    /**
     * 修改参数值
     */
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/sysConfig")
    public AjaxResult updateConfig(@RequestBody SysConfigDto sysConfigDto) {
        return toAjax(configService.updateSysConfig(sysConfigDto));
    }

    /**
     * 删除参数配置
     */
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable("configIds") Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 新增参数配置
     */
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfigDto sysConfigDto) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigId(sysConfig.getConfigId());
        sysConfig.setConfigKey(sysConfigDto.getConfigKey());
        if (!configService.checkConfigKeyUnique(sysConfig)) {
            return error("新增参数'" + sysConfigDto.getConfigName() + "'失败，参数键名已存在");
        }
        return toAjax(configService.insertConfig(sysConfigDto));
    }

}
