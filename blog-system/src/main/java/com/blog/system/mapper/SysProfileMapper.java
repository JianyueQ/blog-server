package com.blog.system.mapper;

import com.blog.common.core.domain.entity.Administrators;
import com.blog.system.domain.dto.UpdateProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 31373
 */
@Mapper
public interface SysProfileMapper {
    int updateAvatarForAdmin(@Param("adminId") Long adminId,@Param("url") String url);

    Administrators selectUserInfoForAdminByAdminId(@Param("adminId") Long adminId);

    int updateProfile(UpdateProfile updateProfile);

    int changePassword(@Param("adminId") Long adminId,@Param("newPassword") String newPassword);
}
