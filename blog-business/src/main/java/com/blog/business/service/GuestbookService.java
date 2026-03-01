package com.blog.business.service;

import com.blog.business.domain.dto.FrontGuestbookListDto;
import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.domain.vo.FrontGuestbookListVo;
import com.blog.business.domain.vo.GuestbookListVo;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;

import java.util.List;

/**
 * @author 31373
 */
public interface GuestbookService {

    Guestbook addMessage(GuestbookDto guestbookDto);

    Guestbook adminReplyMessage(GuestbookDto guestbookDto);

    int updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto);

    int deleteGuestbookMessage(Long id);

    AjaxResult getFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto);

    /**
     * 缓存全部前台子评论
     */
    void cacheAllFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto);

    /**
     * 缓存全部子评论
     */
    void cacheAllChildGuestbookList(GuestbookListDto guestbookListDto);

    AjaxResult getFrontRootGuestbookList(FrontGuestbookListDto frontGuestbookListDto);

    List<GuestbookListVo> getRootGuestbookList(GuestbookListDto guestbookListDto);

    List<GuestbookListVo> getChildGuestbookList(GuestbookListDto guestbookListDto);
}
