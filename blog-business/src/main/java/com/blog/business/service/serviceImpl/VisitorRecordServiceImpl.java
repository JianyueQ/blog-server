package com.blog.business.service.serviceImpl;

import com.blog.business.domain.dto.VisitorRecordDto;
import com.blog.business.domain.dto.VisitorRecordListDto;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.domain.vo.VisitorRecordDetailVo;
import com.blog.business.domain.vo.VisitorRecordVo;
import com.blog.business.mapper.VisitorRecordMapper;
import com.blog.business.service.VisitorRecordService;
import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.UserConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.exception.ServiceException;
import com.blog.common.utils.MessageUtils;
import com.blog.common.utils.SecurityUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31373
 */
@Service
public class VisitorRecordServiceImpl implements VisitorRecordService {

    @Autowired
    private VisitorRecordMapper visitorRecordMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化黑名单到缓存
     */
    @PostConstruct
    public void init() {
        loadVisitorRecordBlacklistCache();
    }

    @Override
    public void insertVisitorRecord(VisitorRecord visitorRecord) {
        visitorRecordMapper.insertVisitorRecord(visitorRecord);
    }

    @Override
    public List<VisitorRecordVo> getVisitorRecordList(VisitorRecordListDto visitorRecordListDto) {
        return visitorRecordMapper.getVisitorRecordList(visitorRecordListDto);
    }

    @Override
    public VisitorRecordDetailVo getVisitorRecordDetail(String visitorRecordId) {
        return visitorRecordMapper.getVisitorRecordDetail(visitorRecordId);
    }

    @Override
    public void cleanVisitorRecord() {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUserOnAdmin().getAdminId())) {
            throw new ServiceException(MessageUtils.message("no.delete.permission"));
        }
        visitorRecordMapper.cleanVisitorRecord();
    }

    @Override
    public void loadVisitorRecordBlacklistCache() {
        VisitorRecordListDto visitorRecordListDto = new VisitorRecordListDto();
        visitorRecordListDto.setBlacklist(UserConstants.YES);
        List<VisitorRecordVo> visitorRecordList = visitorRecordMapper.getVisitorRecordList(visitorRecordListDto);
        if (!visitorRecordList.isEmpty()) {
            List<String> ipList = visitorRecordList.stream().map(VisitorRecordVo::getIpaddr).toList();
            redisCache.setCacheList(CacheConstants.VISITOR_RECORD_BLACKLIST, ipList);
        }
    }

    @Override
    public int updateBlacklist(VisitorRecordDto visitorRecordDto) {
        int i = visitorRecordMapper.updateBlacklist(visitorRecordDto);
        redisCache.deleteObject(CacheConstants.VISITOR_RECORD_BLACKLIST);
        loadVisitorRecordBlacklistCache();
        return i;
    }
}
