package com.blog.business.mapper;

import com.blog.business.domain.dto.FrontGuestbookListDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.domain.vo.FrontGuestbookListVo;
import com.blog.business.domain.vo.GuestbookListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 31373
 */
@Mapper
public interface GuestbookMapper {
    int addMessage(Guestbook guestbook);

    int updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto);

    int deleteGuestbookMessage(Long id);



    Guestbook getGuestbookMessageById(@Param("guestbookId") Long id);

    int deleteGuestbookMessageByIds(List<Guestbook> guestbookList);

    List<Guestbook> getChildGuestbookMessageIdsById(@Param("guestbookId") Long guestbookId);

    List<GuestbookListVo> getRootGuestbookList(GuestbookListDto guestbookListDto);

    List<GuestbookListVo> getChildGuestbookList(GuestbookListDto guestbookListDto);

    List<Guestbook> getFrontRootGuestbookList(FrontGuestbookListDto frontGuestbookListDto);

    List<Guestbook> getFrontChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto);

    Integer getFrontRootGuestbookListCount(FrontGuestbookListDto frontGuestbookListDto);

    List<FrontGuestbookListVo> getReplyCountsByRootIds(@Param("rootIds") List<Long> rootIds);

    void updateReplyCount(@Param("rootId") Long rootId);

}
