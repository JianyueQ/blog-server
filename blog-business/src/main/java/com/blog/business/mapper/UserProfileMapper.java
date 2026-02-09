package com.blog.business.mapper;

import com.blog.business.domain.vo.BlogAboutMeVo;
import com.blog.business.domain.vo.BlogOnerProfileVO;
import com.blog.business.domain.vo.BlogSocialLinkVo;
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

    List<BlogSocialLinkVo> getBlogOwnerSocialInfo();

    BlogAboutMeVo getBlogOwnerAboutMe();

}
