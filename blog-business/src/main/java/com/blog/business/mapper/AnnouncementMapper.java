package com.blog.business.mapper;

import com.blog.business.domain.dto.AnnouncementDto;
import com.blog.business.domain.entity.Announcement;
import com.blog.business.domain.vo.AnnouncementVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 31373
 */
@Mapper
public interface AnnouncementMapper {
    int updateAnnouncement(Announcement announcement);

    AnnouncementVo getAnnouncement();

}
