package com.blog.web.controller.system;

import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import com.blog.system.domain.dto.Collaborators;
import com.blog.system.domain.dto.ResetPwd;
import com.blog.system.domain.vo.AdministratorsVO;
import com.blog.system.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 获取后台管理用户列表
     */
    @GetMapping("/list")
    public TableDataInfo getAdminList() {
        startPage();
        List<AdministratorsVO> list = sysUserService.selectAdminList();
        return getDataTable(list);
    }

    /**
     * 新增后台管理协助者账号
     */
    @Log(title = "后台协助者账号管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addCollaborators(@RequestBody Collaborators collaborators) {
        return success(sysUserService.addCollaborators(collaborators) > 0);
    }

    /**
     * 修改后台管理协助者账号
     */
    @Log(title = "后台协助者账号管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateCollaborators(@RequestBody Collaborators collaborators) {
        return success(sysUserService.updateCollaborators(collaborators) > 0);
    }

    /**
     * 删除后台管理协助者账号
     */
    @Log(title = "后台协助者账号管理", businessType = BusinessType.DELETE)
    @DeleteMapping("{id}")
    public AjaxResult deleteCollaborators(@PathVariable("id") Long id) {
        return success(sysUserService.deleteCollaborators(id) > 0);
    }

    /**
     * 获取后台管理协助者账号信息
     */
    @GetMapping("{id}")
    public AjaxResult getCollaborators(@PathVariable("id") Long id) {
        Collaborators collaborators = sysUserService.getCollaborators(id);
        return AjaxResult.success(collaborators);
    }

    /**
     * 重置后台管理协助者账号的密码
     */
    @Log(title = "后台协助者账号管理", businessType = BusinessType.UPDATE)
    @PutMapping("resetPwd")
    public AjaxResult resetPassword(@RequestBody ResetPwd resetPwd) {
        Long adminId = resetPwd.getAdminId();
        String password = resetPwd.getPassword();
        return success(sysUserService.resetPassword(adminId,password) > 0);
    }
}
