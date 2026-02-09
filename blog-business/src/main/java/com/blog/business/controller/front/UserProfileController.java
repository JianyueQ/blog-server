package com.blog.business.controller.front;

import com.blog.business.domain.vo.BlogAboutMeVo;
import com.blog.business.domain.vo.BlogSocialLinkVo;
import com.blog.business.service.UserProfileService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台用户信息处理
 *
 * @author 31373
 */
@Anonymous
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
    @GetMapping("/getBlogOwnerProfile")
    public AjaxResult getBlogOwnerProfile() {
        return AjaxResult.success(userProfileService.getBlogOwnerProfile());
    }


    /**
     * 获取博主的社交联系信息
     */
    @GetMapping("/getBlogOwnerSocialInfo")
    public AjaxResult getBlogOwnerSocialInfo() {
        List<BlogSocialLinkVo> socialLinkVo = userProfileService.getBlogOwnerSocialInfo();
        return AjaxResult.success(socialLinkVo);
    }

    /**
     * 获取博主的关于我信息
     */
    @GetMapping("/getBlogOwnerAboutMe")
    public AjaxResult getBlogOwnerAboutMe() {
        BlogAboutMeVo aboutMeVo = userProfileService.getBlogOwnerAboutMe();
        return AjaxResult.success(aboutMeVo);
    }
}
