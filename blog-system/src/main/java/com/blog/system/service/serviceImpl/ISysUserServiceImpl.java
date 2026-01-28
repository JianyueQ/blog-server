package com.blog.system.service.serviceImpl;

import com.blog.common.core.domain.entity.Administrators;
import com.blog.system.mapper.ISysUserMapper;
import com.blog.system.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 *
 * @author 31373
 */
@Service
public class ISysUserServiceImpl implements ISysUserService {

    private final ISysUserMapper sysUserMapper;

    public ISysUserServiceImpl(ISysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public Administrators selectUserByUserName(String username) {
        return sysUserMapper.selectUserByUserName(username);
    }
}
