package com.blog.system.service.serviceImpl;

import com.blog.common.core.domain.entity.SysOperLog;
import com.blog.system.mapper.SysOperLogMapper;
import com.blog.system.service.SysOperLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志记录
 * @author 31373
 */
@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    private final SysOperLogMapper sysOperLogMapper;

    public SysOperLogServiceImpl(SysOperLogMapper sysOperLogMapper) {
        this.sysOperLogMapper = sysOperLogMapper;
    }

    @Override
    public void insertOperlog(SysOperLog operLog) {
        sysOperLogMapper.insertOperlog(operLog);
    }
}
