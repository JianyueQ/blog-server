package com.blog.system.service;

import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.system.domain.vo.SysUserOnlineVo;

/**
 * @author 31373
 */
public interface SysUserOnlineService {
    SysUserOnlineVo selectOnlineByInfo(String ipaddr, String userName, LoginUserOnAdmin user);

    SysUserOnlineVo selectOnlineByIpaddr(String ipaddr, LoginUserOnAdmin user);

    SysUserOnlineVo selectOnlineByUserName(String userName, LoginUserOnAdmin user);

    SysUserOnlineVo loginUserToUserOnline(LoginUserOnAdmin user);

}
