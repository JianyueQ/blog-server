package com.blog.business.service;

import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.vo.FrontGuestbookListVo;
import com.blog.business.domain.vo.GuestbookListVo;

import java.util.List;

/**
 * @author 31373
 */
public interface GuestbookService {
    List<GuestbookListVo> getGuestbookList(GuestbookListDto guestbookListDto);

    int addMessage(GuestbookDto guestbookDto);

    int adminReplyMessage(GuestbookDto guestbookDto);

    int updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto);

    int deleteGuestbookMessage(Long id);

    List<FrontGuestbookListVo> getFrontGuestbookList();

}
