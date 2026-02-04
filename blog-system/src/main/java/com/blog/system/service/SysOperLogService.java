package com.blog.system.service;

import com.blog.common.core.domain.entity.SysOperLog;

/**
 * 操作日志记录
 * @author 31373
 */
public interface SysOperLogService {
    void insertOperlog(SysOperLog operLog);
}
