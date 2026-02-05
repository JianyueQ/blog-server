package com.blog.system.service;

import com.blog.common.core.domain.entity.Administrators;
import com.blog.system.domain.dto.Collaborators;
import com.blog.system.domain.vo.AdministratorsVO;

import java.util.List;

/**
 * 用户服务层
 * @author 31373
 */
public interface SysUserService {
    Administrators selectUserByUserName(String username);

    List<AdministratorsVO> selectAdminList();

    int addCollaborators(Collaborators collaborators);

    int updateCollaborators(Collaborators collaborators);

    int deleteCollaborators(Long id);

    Collaborators getCollaborators(Long id);

    int resetPassword(Long adminId, String password);
}
