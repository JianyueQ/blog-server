package com.blog.system.service;

import com.blog.common.core.domain.entity.Administrators;
import com.blog.system.domain.ChangePassword;
import com.blog.system.domain.UpdateProfile;

/**
 * 个人信息 业务层
 *
 * @author 31373
 */
public interface SysProfileService {
    boolean updateAvatarForAdmin(Long adminId, String url);


    int updateProfile(UpdateProfile updateProfile);

    Administrators selectUserInfoForAdminByAdminId(Long adminId);

    int changePassword(Long adminId, String newPassword);
}
