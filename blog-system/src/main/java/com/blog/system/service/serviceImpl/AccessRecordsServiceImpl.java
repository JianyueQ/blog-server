package com.blog.system.service.serviceImpl;

import com.blog.common.core.domain.entity.SysLogininfor;
import com.blog.system.domain.dto.SysLogininforDto;
import com.blog.system.domain.vo.SysLogininforVo;
import com.blog.system.mapper.AccessRecordsMapper;
import com.blog.system.service.AccessRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 访问记录
 * @author 31373
 */
@Service
public class AccessRecordsServiceImpl implements AccessRecordsService {

    @Autowired
    private AccessRecordsMapper accessRecordsMapper;

    @Override
    public List<SysLogininforVo> selectAccessRecordsList(SysLogininforDto logininforDto) {
        return accessRecordsMapper.selectAccessRecordsList(logininforDto);
    }

    @Override
    public int deleteAccessRecordsByIds(Long[] infoIds) {
        return accessRecordsMapper.deleteAccessRecordsByIds(infoIds);
    }

    @Override
    public void cleanAccessRecords() {
        accessRecordsMapper.cleanAccessRecords();
    }

    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        accessRecordsMapper.insertLogininfor(logininfor);
    }
}
