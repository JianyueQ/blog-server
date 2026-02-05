package com.blog.system.service;

import com.blog.common.core.domain.entity.SysOperLog;

import java.util.List;

/**
 * 操作日志记录
 * @author 31373
 */
public interface SysOperLogService {
    void insertOperlog(SysOperLog operLog);

    List<SysOperLog> selectOperLogList(SysOperLog operLog);

    int deleteOperLogByIds(Long[] operIds);

    void cleanOperLog();

    SysOperLog selectOperLogById(Long operId);
}
