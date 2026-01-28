package com.blog.system.service;

import com.blog.common.core.domain.entity.Administrators;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户服务层
 * @author 31373
 */
public interface ISysUserService {
    Administrators selectUserByUserName(String username);
}
