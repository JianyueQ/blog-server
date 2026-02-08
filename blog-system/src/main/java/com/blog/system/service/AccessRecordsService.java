package com.blog.system.service;

import com.blog.common.core.domain.entity.SysLogininfor;
import com.blog.system.domain.dto.SysLogininforDto;
import com.blog.system.domain.vo.SysLogininforVo;

import java.util.List;

/**
 * @author 31373
 */
public interface AccessRecordsService {
    List<SysLogininforVo> selectAccessRecordsList(SysLogininforDto logininforDto);

    int deleteAccessRecordsByIds(Long[] infoIds);

    void cleanAccessRecords();

    void insertLogininfor(SysLogininfor logininfor);
}
