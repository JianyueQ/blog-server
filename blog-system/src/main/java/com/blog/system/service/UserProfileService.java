package com.blog.system.service;

import com.blog.system.domain.vo.AboutMeVo;
import com.blog.system.domain.vo.BlogOnerProfileVO;
import com.blog.system.domain.vo.SocialLinkVo;

import java.util.List;

/**
 * 前台用户信息处理业务层
 *
 * @author 31373
 */
public interface UserProfileService {

    /**
     * 获取博主的个人信息
     * @return 博主的个人信息
     */
    BlogOnerProfileVO getBlogOwnerProfile();

    List<SocialLinkVo> getBlogOwnerSocialInfo();

    AboutMeVo getBlogOwnerAboutMe();

}
