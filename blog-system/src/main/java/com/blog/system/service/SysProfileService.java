package com.blog.system.service;

/**
 * 个人信息 业务层
 *
 * @author 31373
 */
public interface SysProfileService {
    boolean updateAvatarForAdmin(Long adminId, String url);
}
