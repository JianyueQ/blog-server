package com.blog.system.mapper;

import com.blog.common.core.domain.entity.Administrators;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表Mapper接口
 * @author 31373
 */
@Mapper
public interface ISysUserMapper {
    Administrators selectUserByUserName(@Param("username") String username);
}
