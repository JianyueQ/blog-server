package com.blog.business.mapper;

import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.domain.vo.FrontGuestbookListVo;
import com.blog.business.domain.vo.GuestbookListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface GuestbookMapper {
    List<GuestbookListVo> getGuestbookList(GuestbookListDto guestbookListDto);

    int addMessage(Guestbook guestbook);

    int updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto);

    int deleteGuestbookMessage(Long id);

    List<FrontGuestbookListVo> getFrontGuestbookList();

    Guestbook getGuestbookMessageById(Long id);

    int deleteGuestbookMessageByIds(@Param("ids") List<Long> ids);
}
