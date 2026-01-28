package com.blog.framework.security.context;

import org.springframework.security.core.Authentication;

/**
 * 认证信息上下文持有者
 * @author 31373
 */
public class AuthenticationContextHolder {

    private static final ThreadLocal<Authentication> AUTHENTICATION_HOLDER = new ThreadLocal<>();

    public static void setAuthenticationHolder(Authentication context){
        AUTHENTICATION_HOLDER.set(context);
    }

    public static Authentication getAuthenticationHolder(){
        return AUTHENTICATION_HOLDER.get();
    }
    public static void clearAuthenticationHolder(){
        AUTHENTICATION_HOLDER.remove();
    }
}
