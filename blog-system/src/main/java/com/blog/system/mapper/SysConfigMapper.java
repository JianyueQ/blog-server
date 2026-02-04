package com.blog.system.mapper;

import com.blog.common.core.domain.entity.AboutMe;
import com.blog.common.core.domain.entity.SocialLink;
import com.blog.system.domain.vo.AboutMeVo;
import com.blog.system.domain.vo.SocialLinkVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface SysConfigMapper {
    int addSocial(SocialLink socialLink);

    int updateSocial(SocialLink socialLink);

    int deleteSocial(@Param("id") String id);

    List<SocialLinkVo> getSocialLink();

    int updateSocialStatus(@Param("socialLinkId") Long socialLinkId, @Param("status") Integer status);

    AboutMeVo getAboutMeContent();

    int updateAboutMeContent(AboutMe aboutMe);

    int addAboutMeContent(AboutMe aboutMe);
}
