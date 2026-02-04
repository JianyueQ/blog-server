package com.blog.system.mapper;

import com.blog.common.core.domain.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志数据层
 * @author 31373
 */
@Mapper
public interface SysOperLogMapper {
    void insertOperlog(SysOperLog operLog);
}
