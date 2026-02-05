package com.blog.system.mapper;

import com.blog.common.core.domain.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作日志数据层
 * @author 31373
 */
@Mapper
public interface SysOperLogMapper {
    void insertOperlog(SysOperLog operLog);

    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    int deleteOperLogByIds(Long[] operIds);

    void cleanOperLog();

    SysOperLog selectOperLogById(Long operId);
}
