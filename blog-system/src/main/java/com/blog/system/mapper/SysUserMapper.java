package com.blog.system.mapper;

import com.blog.common.core.domain.entity.Administrators;
import com.blog.system.domain.dto.Collaborators;
import com.blog.system.domain.vo.AdministratorsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表Mapper接口
 * @author 31373
 */
@Mapper
public interface SysUserMapper {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    Administrators selectUserByUserName(@Param("username") String username);

    /**
     * 更新管理员登录信息
     * @param adminId 管理员ID
     * @param loginTime 登录时间
     * @param ipaddr 登录ip
     */
    void updateAdminDetailed(@Param("adminId") String adminId, @Param("loginTime") String loginTime, @Param("ipaddr") String ipaddr);

    List<AdministratorsVO> selectAdminList();

    int addCollaborators(Administrators administrators);

    int updateCollaborators(Administrators administrators);

    int deleteCollaborators(Long id);

    Collaborators getCollaboratorsById(@Param("adminId") Long adminId);

    int resetPassword(@Param("adminId") Long adminId, @Param("encryptPassword") String encryptPassword);
}
