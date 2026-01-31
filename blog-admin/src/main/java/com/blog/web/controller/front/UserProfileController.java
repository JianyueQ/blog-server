package com.blog.web.controller.front;

import com.blog.common.domain.AjaxResult;
import com.blog.system.service.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台用户信息处理
 *
 * @author 31373
 */
@RestController
@RequestMapping("/blog/user/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;


    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    /**
     * 获取博主的个人信息
     */
    @GetMapping("getBlogOwnerProfile")
    public AjaxResult getBlogOwnerProfile() {
        return AjaxResult.success(userProfileService.getBlogOwnerProfile());
    }
}
