package com.blog.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 31373
 */
@Mapper
public interface SysProfileMapper {
    int updateAvatarForAdmin(@Param("adminId") Long adminId,@Param("url") String url);
}
