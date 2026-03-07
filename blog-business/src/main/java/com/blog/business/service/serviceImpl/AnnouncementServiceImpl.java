package com.blog.business.service.serviceImpl;

import com.blog.business.domain.dto.AnnouncementDto;
import com.blog.business.domain.entity.Announcement;
import com.blog.business.domain.vo.AnnouncementVo;
import com.blog.business.mapper.AnnouncementMapper;
import com.blog.business.service.AnnouncementService;
import com.blog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author 31373
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    @CacheEvict(cacheNames = "announcement", key = "'announcement'")
    public int updateAnnouncement(AnnouncementDto announcementDto) {
        Announcement announcement = new Announcement();
        announcement.setContent(announcementDto.getContent());
        announcement.setUpdateTime(DateUtils.getNowDate());
        return announcementMapper.updateAnnouncement(announcement);
    }

    @Override
    @Cacheable(cacheNames = "announcement", key = "'announcement'")
    public AnnouncementVo getAnnouncement() {
        return announcementMapper.getAnnouncement();
    }
}
