package com.blog.system.service;

import com.blog.system.domain.dto.SocialLinkDto;
import com.blog.system.domain.vo.AboutMeVo;
import com.blog.system.domain.vo.SocialLinkVo;

import java.util.List;

/**
 * 系统配置服务层
 * @author 31373
 */
public interface SysConfigService {

    List<SocialLinkVo> getSocialLink();

    int addSocial(SocialLinkDto socialLinkDto);

    int updateSocial(SocialLinkDto socialLinkDto);

    int deleteSocial(String id);

    int updateSocialStatus(Long socialLinkId, Integer status);

    AboutMeVo getAboutMeContent();

    int updateAboutMeContent(String aboutMeId, String aboutMeContent);
}
