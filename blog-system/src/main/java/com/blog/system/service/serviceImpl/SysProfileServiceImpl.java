package com.blog.system.service.serviceImpl;

import com.blog.system.mapper.SysProfileMapper;
import com.blog.system.service.SysProfileService;
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

    @Override
    public boolean updateAvatarForAdmin(Long adminId, String url) {
        return sysProfileMapper.updateAvatarForAdmin(adminId, url) > 0;
    }
}
