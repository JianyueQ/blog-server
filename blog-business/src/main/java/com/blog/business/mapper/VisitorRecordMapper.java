package com.blog.business.mapper;

import com.blog.business.domain.dto.VisitorRecordDto;
import com.blog.business.domain.dto.VisitorRecordListDto;
import com.blog.business.domain.entity.VisitorInfo;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.domain.vo.VisitorRecordDetailVo;
import com.blog.business.domain.vo.VisitorRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 访客记录Mapper
 *
 * @author 31373
 */
@Mapper
public interface VisitorRecordMapper {
    void insertVisitorRecord(VisitorRecord visitorRecord);

    List<VisitorRecordVo> getVisitorRecordList(VisitorRecordListDto visitorRecordListDto);

    VisitorRecordDetailVo getVisitorRecordDetail(@Param("visitorRecordId") String visitorRecordId);

    void cleanVisitorRecord();

    int updateBlacklist(VisitorRecordDto visitorRecordDto);

    VisitorRecord getVisitorRecordByFingerprint(@Param("fingerprint") String fingerprint);

    void updateVisitorRecord(VisitorRecord visitorRecordInDB);

    void updateVisitorInfo(VisitorInfo visitorInfo);

    void insertVisitorInfo(VisitorInfo visitorInfo);
}
