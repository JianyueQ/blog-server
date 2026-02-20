package com.blog.system.service.serviceImpl;

import com.blog.common.constant.CacheConstants;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.system.domain.dto.UpdateProfile;
import com.blog.system.mapper.SysProfileMapper;
import com.blog.system.service.SysProfileService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * 个人信息 业务层实现
 *
 * @author 31373
 */
@Service
public class SysProfileServiceImpl implements SysProfileService {

    private final SysProfileMapper sysProfileMapper;


    public SysProfileServiceImpl(SysProfileMapper sysProfileMapper) {
        this.sysProfileMapper = sysProfileMapper;
    }


    @CacheEvict(cacheNames = CacheConstants.CACHE_BLOG_OWNER_PROFILE,key = "'front_user_profile'")
    @Override
    public boolean updateAvatarForAdmin(Long adminId, String url) {
        return sysProfileMapper.updateAvatarForAdmin(adminId, url) > 0;
    }

    @CacheEvict(cacheNames = CacheConstants.CACHE_BLOG_OWNER_PROFILE,key = "'front_user_profile'")
    @Override
    public int updateProfile(UpdateProfile updateProfile) {
        return sysProfileMapper.updateProfile(updateProfile);
    }

    @Override
    public Administrators selectUserInfoForAdminByAdminId(Long adminId) {
        return sysProfileMapper.selectUserInfoForAdminByAdminId(adminId);
    }

    @Override
    public int changePassword(Long adminId, String newPassword) {
        return sysProfileMapper.changePassword(adminId, newPassword);
    }


}
