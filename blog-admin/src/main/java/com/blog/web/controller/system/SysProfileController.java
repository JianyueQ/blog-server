package com.blog.web.controller.system;

import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.file.FileUploadUtils;
import com.blog.common.utils.file.MimeTypeUtils;
import com.blog.framework.web.service.MinioService;
import com.blog.framework.web.service.TokenService;
import com.blog.system.service.SysProfileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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



}
