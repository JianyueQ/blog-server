package com.blog.business.service;

import com.blog.business.domain.dto.VisitorRecordDto;
import com.blog.business.domain.dto.VisitorRecordListDto;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.domain.vo.VisitorRecordDetailVo;
import com.blog.business.domain.vo.VisitorRecordVo;

import java.util.List;

/**
 * @author 31373
 */
public interface VisitorRecordService {
    void insertVisitorRecord(VisitorRecord visitorRecord);

    List<VisitorRecordVo> getVisitorRecordList(VisitorRecordListDto visitorRecordListDto);

    VisitorRecordDetailVo getVisitorRecordDetail(String visitorRecordId);

    void cleanVisitorRecord();

    /**
     * 获取黑名单列表
     */
    void loadVisitorRecordBlacklistCache();

    int updateBlacklist(VisitorRecordDto visitorRecordDto);
}
