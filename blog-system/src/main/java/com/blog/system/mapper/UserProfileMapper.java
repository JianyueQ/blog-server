package com.blog.system.mapper;

import com.blog.system.domain.vo.AboutMeVo;
import com.blog.system.domain.vo.BlogOnerProfileVO;
import com.blog.system.domain.vo.SocialLinkVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 前台用户信息持久层
 *
 * @author 31373
 */
@Mapper
public interface UserProfileMapper {

    /**
     * 获取博主的个人信息
     * @return 博主的个人信息
     */
    BlogOnerProfileVO getBlogOwnerProfile();

    List<SocialLinkVo> getBlogOwnerSocialInfo();

    AboutMeVo getBlogOwnerAboutMe();

}
