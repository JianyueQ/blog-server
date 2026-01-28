package com.blog.framework.web.service;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

/**
 * 多用户认证 token
 *
 * @author 31373
 */
public class MultiUserAuthenticationToken extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户信息
     */
    private final Object principal;
    /**
     * 密码
     */
    private final Object credentials;

    /**
     * 用户类型
     */
    private final String userType;

    /**
     * 无权限信息的认证
     *
     * @param principal   用户信息
     * @param credentials 凭证
     * @param userType    用户类型
     */
    public MultiUserAuthenticationToken(Object principal, Object credentials, String userType) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.userType = userType;
        setAuthenticated(false);
    }

    /**
     * 有权限信息的认证
     *
     * @param principal   用户信息
     * @param credentials 凭证
     * @param authorities 权限信息
     * @param userType    用户类型
     */
    public MultiUserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String userType) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.userType = userType;
        setAuthenticated(true);
    }


    /**
     * 获取凭证
     *
     * @return 凭证
     */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    /**
     * 用户信息
     *
     * @return 用户信息
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public String getUserType() {
        return this.userType;
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    @Override
    public Object getDetails() {
        return this.userType;
    }

    /**
     * 设置认证信息
     * @param isAuthenticated 是否认证
     * @throws IllegalArgumentException 如果尝试将令牌设置为可信
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(false);
    }
}
