package com.blog.web.controller.system;

import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.file.MimeTypeUtils;
import com.blog.framework.web.service.MinioService;
import com.blog.framework.web.service.TokenService;
import com.blog.system.domain.ChangePassword;
import com.blog.system.domain.UpdateProfile;
import com.blog.system.service.SysProfileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息 业务处理
 *
 * @author 31373
 */

@RestController
@RequestMapping("/system/admin/profile")
public class SysProfileController extends BaseController {

    private final MinioService minioService;
    private final SysProfileService sysProfileService;
    private final TokenService tokenService;

    public SysProfileController(MinioService minioService, SysProfileService sysProfileService, TokenService tokenService) {
        this.minioService = minioService;
        this.sysProfileService = sysProfileService;
        this.tokenService = tokenService;
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile avatar) throws Exception {
        if (!avatar.isEmpty()) {
            //获取登录的管理员用户缓存信息
            LoginUserOnAdmin loginUser = getLoginUserOnAdmin();
            //上传头像
            String url = minioService.uploadAvatar(avatar, MimeTypeUtils.IMAGE_EXTENSION, true);

            if (sysProfileService.updateAvatarForAdmin(loginUser.getAdminId(),url)){
                String oldAvatar = loginUser.getAdministrators().getAvatar();
                if (StringUtils.isNotEmpty(oldAvatar))
                {
                    minioService.deleteFile(oldAvatar);
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", url);
                // 更新缓存用户头像
                loginUser.getAdministrators().setAvatar(url);
                tokenService.setLoginUserOnAdmin(loginUser);
                return ajax;
            }
        }
        return error("上传图片异常");
    }

    /**
     * 基本资料修改
     */
    @PutMapping
    public AjaxResult updateProfile(@RequestBody UpdateProfile updateProfile) {
        if (StringUtils.isNotNull(updateProfile)) {
            //获取登录的管理员用户缓存信息
            LoginUserOnAdmin loginUser = getLoginUserOnAdmin();
            // 更新缓存用户头像
            loginUser.getAdministrators().setNickname(updateProfile.getNickname());
            loginUser.getAdministrators().setEmail(updateProfile.getEmail());
            loginUser.getAdministrators().setDescription(updateProfile.getDescription());
            updateProfile.setAdminId(loginUser.getAdminId());
            if (sysProfileService.updateProfile(updateProfile) > 0) {
                tokenService.setLoginUserOnAdmin(loginUser);
                return success();
            }
        }
        return error("修改失败");
    }

    /**
     * 修改密码
     */
    @PutMapping("changePassword")
    public AjaxResult changePassword(@RequestBody ChangePassword changePassword) {
        if (StringUtils.isNotNull(changePassword)) {
            LoginUserOnAdmin loginUserOnAdmin = getLoginUserOnAdmin();
            Long adminId = loginUserOnAdmin.getAdminId();
            Administrators administrators = sysProfileService.selectUserInfoForAdminByAdminId(adminId);
            String oldPassword = changePassword.getOldPassword();
            String newPassword = changePassword.getNewPassword();
            String password = administrators.getPassword();
            if (!SecurityUtils.matchesPassword(oldPassword, password)) {
                return error("修改失败，旧密码错误");
            }
            if (SecurityUtils.matchesPassword(newPassword, password)) {
                return error("新密码不能与旧密码相同");
            }
            newPassword = SecurityUtils.encryptPassword(newPassword);
            if (sysProfileService.changePassword(adminId, newPassword) > 0) {
                tokenService.delLoginUserOnAdmin(loginUserOnAdmin.getToken());
                return success();
            }
        }
        return error("修改失败");
    }
}
