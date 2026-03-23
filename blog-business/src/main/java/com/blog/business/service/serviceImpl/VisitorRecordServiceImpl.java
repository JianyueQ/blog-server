package com.blog.business.service.serviceImpl;

import com.alibaba.fastjson2.JSON;
import com.blog.business.domain.dto.VisitorRecordDto;
import com.blog.business.domain.dto.VisitorRecordListDto;
import com.blog.business.domain.entity.VisitorRecord;
import com.blog.business.domain.entity.VisitorRecordParameters;
import com.blog.business.domain.vo.VisitorInfoVo;
import com.blog.business.domain.vo.VisitorRecordDetailVo;
import com.blog.business.domain.vo.VisitorRecordVo;
import com.blog.business.mapper.VisitorInfoMapper;
import com.blog.business.mapper.VisitorRecordMapper;
import com.blog.business.rabbitmq.RabbitManager;
import com.blog.business.service.VisitorRecordService;
import com.blog.business.utils.FingerprintUtils;
import com.blog.common.constant.CacheConstants;
import com.blog.common.constant.HttpStatus;
import com.blog.common.constant.UserConstants;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.exception.ServiceException;
import com.blog.common.utils.*;
import com.blog.common.utils.http.UserAgentUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 31373
 */
@Service
public class VisitorRecordServiceImpl implements VisitorRecordService {

    private static final Logger log = LogManager.getLogger(VisitorRecordServiceImpl.class);
    @Autowired
    private VisitorRecordMapper visitorRecordMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private VisitorInfoMapper visitorInfoMapper;
    @Autowired
    private RabbitManager rabbitManager;

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
        VisitorRecordDetailVo visitorRecordDetail = visitorRecordMapper.getVisitorRecordDetail(visitorRecordId);
        VisitorInfoVo visitorInfo = visitorInfoMapper.getVisitorInfoVoById(visitorRecordDetail.getVisitorInfoId());
        visitorRecordDetail.setVisitorInfoVo(visitorInfo);
        return visitorRecordDetail;
    }

    @Override
    @Transactional
    public void cleanVisitorRecord() {
        if (!SecurityUtils.isAdmin(SecurityUtils.getLoginUserOnAdmin().getAdminId())) {
            throw new ServiceException(MessageUtils.message("no.delete.permission"));
        }
        visitorRecordMapper.cleanVisitorRecord();
        visitorInfoMapper.cleanVisitorInfo();
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

    @Override
    public VisitorRecord getVisitorRecordByFingerprint(String fingerprint) {
        return visitorRecordMapper.getVisitorRecordByFingerprint(fingerprint);
    }

    @Override
    public void updateVisitorRecord(VisitorRecord visitorRecordInDB) {
        visitorRecordMapper.updateVisitorRecord(visitorRecordInDB);
    }

    @Override
    public String getPermissions(String clientData) {
        HttpServletRequest request = ServletUtils.getRequest();
        String userAgent = request.getHeader("User-Agent");
        String ipAddr = IpUtils.getIpAddr(request);
        String decrypt = FingerprintUtils.decrypt(clientData);
        VisitorRecordParameters visitorRecordParameters = JSON.parseObject(decrypt, VisitorRecordParameters.class);
        if (StringUtils.isNotNull(visitorRecordParameters)) {
            String browser = UserAgentUtils.getBrowser(userAgent);
            String operatingSystem = UserAgentUtils.getOperatingSystem(userAgent);
            visitorRecordParameters.setBrowser(browser);
            visitorRecordParameters.setOs(operatingSystem);
            String fingerprint = FingerprintUtils.generateFingerprint(visitorRecordParameters);
            if (redisCache.setCacheUniqueValue(cacheKey(ipAddr), fingerprint, 30, TimeUnit.MINUTES)){
                recordVisitor(visitorRecordParameters,fingerprint,ipAddr,userAgent);
            }
            return fingerprint;
        }else {
            throw new ServiceException(HttpStatus.WARN, "你已被加入黑名单,请联系管理员");
        }
    }

    /**
     * 记录访客信息
     */
    private void recordVisitor(VisitorRecordParameters visitorRecordParameters, String fingerprint, String ipAddr, String userAgent) {
        try {
            VisitorRecord visitorRecord = new VisitorRecord();
            visitorRecord.setIpaddr(ipAddr);
            visitorRecord.setLocation(AddressUtils.getRealAddressByIP(ipAddr));
            visitorRecord.setUserAgent(userAgent);
            visitorRecord.setBrowser(UserAgentUtils.getBrowser(userAgent));
            visitorRecord.setOs(UserAgentUtils.getOperatingSystem(userAgent));
            visitorRecord.setVisitTime(DateUtils.getTime());
            visitorRecord.setCreateTime(DateUtils.getNowDate());
            visitorRecord.setFingerprint(fingerprint);
            visitorRecord.setVisitor(visitorRecordParameters.getVisitor());
            // 异步保存访客记录
            rabbitManager.sendVisitorRecord(visitorRecord);
            log.debug("记录访客信息: IP={}", visitorRecord.getIpaddr());
        } catch (Exception e) {
            log.error("记录访客信息失败:", e);
        }
    }

    /**
     * 缓存键
     */
    private String cacheKey(String ip) {
        return CacheConstants.VISITOR_CACHE + ip;
    }
}
