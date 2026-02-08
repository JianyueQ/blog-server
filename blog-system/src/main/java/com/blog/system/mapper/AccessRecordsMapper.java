package com.blog.system.mapper;

import com.blog.common.core.domain.entity.SysLogininfor;
import com.blog.system.domain.dto.SysLogininforDto;
import com.blog.system.domain.vo.SysLogininforVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 访问记录
 * @author 31373
 */
@Mapper
public interface AccessRecordsMapper {

    List<SysLogininforVo> selectAccessRecordsList(SysLogininforDto logininforDto);

    int deleteAccessRecordsByIds(Long[] infoIds);

    void cleanAccessRecords();

    void insertLogininfor(SysLogininfor logininfor);
}
