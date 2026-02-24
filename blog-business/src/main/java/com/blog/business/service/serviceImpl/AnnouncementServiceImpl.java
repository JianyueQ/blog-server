package com.blog.business.service.serviceImpl;

import com.blog.business.domain.dto.AnnouncementDto;
import com.blog.business.domain.entity.Announcement;
import com.blog.business.domain.vo.AnnouncementVo;
import com.blog.business.mapper.AnnouncementMapper;
import com.blog.business.service.AnnouncementService;
import com.blog.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 31373
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public int updateAnnouncement(AnnouncementDto announcementDto) {
        Announcement announcement = new Announcement();
        announcement.setContent(announcementDto.getContent());
        announcement.setUpdateTime(DateUtils.getNowDate());
        return announcementMapper.updateAnnouncement(announcement);
    }

    @Override
    public AnnouncementVo getAnnouncement() {
        return announcementMapper.getAnnouncement();
    }
}
