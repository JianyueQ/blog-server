package com.blog.business.service;

import com.blog.business.domain.dto.AnnouncementDto;
import com.blog.business.domain.vo.AnnouncementVo;

/**
 * @author 31373
 */
public interface AnnouncementService {
    int updateAnnouncement(AnnouncementDto announcementDto);

    AnnouncementVo getAnnouncement();

}
