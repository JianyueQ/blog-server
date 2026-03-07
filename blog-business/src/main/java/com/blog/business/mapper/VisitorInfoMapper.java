package com.blog.business.mapper;

import com.blog.business.domain.vo.VisitorInfoVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface VisitorInfoMapper {

    VisitorInfoVo getVisitorInfoVoById(String visitorInfoId);

    void cleanVisitorInfo();

}
