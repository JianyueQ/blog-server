package com.blog.system.service.serviceImpl;

import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.utils.StringUtils;
import com.blog.system.domain.vo.SysUserOnlineVo;
import com.blog.system.service.SysUserOnlineService;
import org.springframework.stereotype.Service;

/**
 * 在线用户ServiceImpl
 *
 * @author 31373
 */
@Service
public class SysUserOnlineServiceImpl implements SysUserOnlineService {
    @Override
    public SysUserOnlineVo selectOnlineByInfo(String ipaddr, String userName, LoginUserOnAdmin user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    @Override
    public SysUserOnlineVo selectOnlineByIpaddr(String ipaddr, LoginUserOnAdmin user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    @Override
    public SysUserOnlineVo selectOnlineByUserName(String userName, LoginUserOnAdmin user) {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    @Override
    public SysUserOnlineVo loginUserToUserOnline(LoginUserOnAdmin user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getAdministrators()))
        {
            return null;
        }
        SysUserOnlineVo sysUserOnline = new SysUserOnlineVo();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        return sysUserOnline;
    }
}
