package com.blog.web.controller.system;

import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import com.blog.system.domain.dto.AboutMeDto;
import com.blog.system.domain.dto.SocialLinkDto;
import com.blog.system.domain.vo.AboutMeVo;
import com.blog.system.domain.vo.SocialLinkVo;
import com.blog.system.service.SysConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    private final SysConfigService sysConfigService;

    public SysConfigController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    /**
     * 获取社交联系信息
     */
    @GetMapping("/social")
    public AjaxResult getSocial() {
        List<SocialLinkVo> socialLinkVo = sysConfigService.getSocialLink();
        return AjaxResult.success(socialLinkVo);
    }

    /**
     * 添加社交联系信息
     */
    @Log(title = "社交联系信息", businessType = BusinessType.INSERT)
    @PostMapping("/social")
    public AjaxResult addSocial(@RequestBody SocialLinkDto socialLinkDto) {
        return success(sysConfigService.addSocial(socialLinkDto) > 0);
    }

    /**
     * 修改社交联系信息
     */
    @Log(title = "社交联系信息", businessType = BusinessType.UPDATE)
    @PutMapping("/social")
    public AjaxResult updateSocial(@RequestBody SocialLinkDto socialLinkDto) {
        return success(sysConfigService.updateSocial(socialLinkDto) > 0);
    }

    /**
     * 删除社交联系信息
     */
    @Log(title = "社交联系信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/social/{id}")
    public AjaxResult deleteSocial(@PathVariable("id") String id) {
        return success(sysConfigService.deleteSocial(id) > 0);
    }

    /**
     * 修改社交联系是否启用状态
     */
    @Log(title = "社交联系信息", businessType = BusinessType.UPDATE)
    @PutMapping("/social/status")
    public AjaxResult updateSocialStatus(@RequestBody SocialLinkDto socialLinkDto) {
        Long socialLinkId = socialLinkDto.getSocialLinkId();
        Integer status = socialLinkDto.getStatus();
        return success(sysConfigService.updateSocialStatus(socialLinkId, status) > 0);
    }

    /**
     * 关于我
     */
    @GetMapping("/aboutMe")
    public AjaxResult getAboutMeContent() {
        AboutMeVo aboutMeVo = sysConfigService.getAboutMeContent();
        return AjaxResult.success(aboutMeVo);
    }

    /**
     * 修改关于我
     */
    @Log(title = "关于我", businessType = BusinessType.UPDATE)
    @PutMapping("/aboutMe")
    public AjaxResult updateAboutMeContent(@RequestBody AboutMeDto aboutMeDto) {
        String aboutMeId = aboutMeDto.getAboutMeId();
        String aboutMeContent = aboutMeDto.getAboutMeContent();
        return success(sysConfigService.updateAboutMeContent(aboutMeId, aboutMeContent) > 0);
    }
}
