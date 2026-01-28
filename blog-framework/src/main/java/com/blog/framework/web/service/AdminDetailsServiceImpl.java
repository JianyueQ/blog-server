package com.blog.framework.web.service;

import com.blog.common.core.domain.entity.Administrators;


import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.enums.UserStatus;
import com.blog.common.exception.ServiceException;
import com.blog.common.utils.MessageUtils;
import com.blog.common.utils.StringUtils;
import com.blog.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author 31373
 */
@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AdminDetailsServiceImpl.class);
    private final ISysUserService userService;
    private final SysPasswordService passwordService;

    public AdminDetailsServiceImpl(ISysUserService userService, SysPasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrators administrators = userService.selectUserByUserName(username);
        //校验用户名
        if (StringUtils.isNull(administrators)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        } else if (UserStatus.DELETE.getCode().equals(administrators.getIsDeleted().toString())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        //校验密码
        passwordService.validateOnAdmin(administrators);
        return createLoginUserOnAdmin(administrators);
    }

    public UserDetails createLoginUserOnAdmin(Administrators administrators) {
        return new LoginUserOnAdmin(administrators.getAdminId(),administrators);
    }
}
