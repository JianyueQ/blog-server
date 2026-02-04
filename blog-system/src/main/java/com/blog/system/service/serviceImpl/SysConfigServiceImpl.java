package com.blog.system.service.serviceImpl;

import com.blog.common.core.domain.entity.AboutMe;
import com.blog.common.core.domain.entity.SocialLink;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.StringUtils;
import com.blog.system.domain.SocialLinkDto;
import com.blog.system.domain.vo.AboutMeVo;
import com.blog.system.domain.vo.SocialLinkVo;
import com.blog.system.mapper.SysConfigMapper;
import com.blog.system.service.SysConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统配置服务层实现
 *
 * @author 31373
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;

    public SysConfigServiceImpl(SysConfigMapper sysConfigMapper) {
        this.sysConfigMapper = sysConfigMapper;
    }

    @Cacheable(cacheNames = "social_link",key = "'sys_config'")
    @Override
    public List<SocialLinkVo> getSocialLink() {
        return sysConfigMapper.getSocialLink();
    }

    @CacheEvict(cacheNames = "social_link",allEntries = true)
    @Override
    public int addSocial(SocialLinkDto socialLinkDto) {
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        SocialLink socialLink = new SocialLink();
        socialLink.setName(socialLinkDto.getName());
        socialLink.setUrl(socialLinkDto.getUrl());
        socialLink.setIcon(socialLinkDto.getIcon());
        socialLink.setTip(socialLinkDto.getTip());
        socialLink.setSortOrder(socialLinkDto.getSortOrder());
        socialLink.setStatus(0);
        socialLink.setCreateBy(String.valueOf(adminId));
        socialLink.setCreateTime(DateUtils.getNowDate());
        return sysConfigMapper.addSocial(socialLink);
    }

    @CacheEvict(cacheNames = "social_link",allEntries = true)
    @Override
    public int updateSocial(SocialLinkDto socialLinkDto) {
        SocialLink socialLink = new SocialLink();
        socialLink.setSocialLinkId(socialLinkDto.getSocialLinkId());
        socialLink.setName(socialLinkDto.getName());
        socialLink.setUrl(socialLinkDto.getUrl());
        socialLink.setIcon(socialLinkDto.getIcon());
        socialLink.setTip(socialLinkDto.getTip());
        socialLink.setSortOrder(socialLinkDto.getSortOrder());
        socialLink.setUpdateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
        socialLink.setUpdateTime(DateUtils.getNowDate());
        return sysConfigMapper.updateSocial(socialLink);
    }

    @CacheEvict(cacheNames = "social_link",allEntries = true)
    @Override
    public int deleteSocial(String id) {
        return sysConfigMapper.deleteSocial(id);
    }

    @CacheEvict(cacheNames = "social_link",allEntries = true)
    @Override
    public int updateSocialStatus(Long socialLinkId, Integer status) {
        return sysConfigMapper.updateSocialStatus(socialLinkId, status);
    }

    @Cacheable(cacheNames = "about_me",key = "'sys_config'")
    @Override
    public AboutMeVo getAboutMeContent() {
        return sysConfigMapper.getAboutMeContent();
    }

    @CacheEvict(cacheNames = "about_me",allEntries = true)
    @Override
    public int updateAboutMeContent(String aboutMeId, String aboutMeContent) {
        AboutMe aboutMe = new AboutMe();
        if (StringUtils.isNull(getAboutMeContent())){
            aboutMe.setAboutMeContent(aboutMeContent);
            aboutMe.setCreateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
            aboutMe.setCreateTime(DateUtils.getNowDate());
            return sysConfigMapper.addAboutMeContent(aboutMe);
        }
        aboutMe.setAboutMeId(aboutMeId);
        aboutMe.setAboutMeContent(aboutMeContent);
        aboutMe.setUpdateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
        aboutMe.setUpdateTime(DateUtils.getNowDate());
        return sysConfigMapper.updateAboutMeContent(aboutMe);
    }
}
