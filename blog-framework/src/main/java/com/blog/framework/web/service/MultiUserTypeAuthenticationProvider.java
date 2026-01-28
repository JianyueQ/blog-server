package com.blog.framework.web.service;

import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.domain.model.LoginUserOnUser;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 多用户类型认证提供者
 *
 * @author 31373
 */
@Component
public class MultiUserTypeAuthenticationProvider implements AuthenticationProvider {

    @Resource(name = "adminDetailsServiceImpl")
    private AdminDetailsServiceImpl adminDetailsServiceImpl;

    @Resource(name = "userDetailsServiceImpl")
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * 认证方法
     *
     * @param authentication 认证对象
     * @return 认证对象
     * @throws AuthenticationException 认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MultiUserAuthenticationToken authToken = (MultiUserAuthenticationToken) authentication;

        // 获取用户名、密码和用户类型
        String username = authToken.getName();
        String userType = authToken.getDetails().toString();

        // 根据用户类型加载用户信息
        UserDetails userDetails = loadUserByUsernameAndType(username, userType);

        // 创建认证成功的token
        return new MultiUserAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities(),
                userType
        );
    }

    public UserDetails loadUserByUsernameAndType(String username, String userType) {
        return switch (userType) {
            case "admin" ->
                // 管理员用户加载逻辑
                    adminDetailsServiceImpl.loadUserByUsername(username);
            case "user" ->
                // 普通用户加载逻辑
                    userDetailsServiceImpl.loadUserByUsername(username);
            default -> throw new UsernameNotFoundException("不支持的用户类型");
        };
    }

    /**
     * 是否支持该类
     *
     * @param authentication 认证对象
     * @return 是否支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // 支持MultiUserAuthenticationToken及其子类
        return MultiUserAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
