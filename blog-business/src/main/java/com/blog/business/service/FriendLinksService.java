package com.blog.business.service;

import com.blog.business.domain.dto.FriendLinksDto;
import com.blog.business.domain.entity.FriendLinks;
import com.blog.business.domain.vo.DisplayedFriendLinksListVo;
import com.blog.business.domain.vo.FriendLinksDetailVo;
import com.blog.business.domain.vo.FriendLinksListVo;

import java.util.List;

/**
 * @author 31373
 */
public interface FriendLinksService {
    List<FriendLinksListVo> getFriendLinksList();

    FriendLinksDetailVo getFriendLinksDetail(Long friendLinksId);

    List<FriendLinksListVo> getApprovedFriendLinksList();

    int addFriendLinks(FriendLinksDto friendLinksDto);

    int updateFriendLinks(FriendLinksDto friendLinksDto);

    int updateFriendLinksStatus(Long friendLinksId, Integer status);

    int deleteFriendLinks(Long friendLinksId);

    List<DisplayedFriendLinksListVo> getDisplayedFriendLinksList();

    int requestToAddFriendLinks(FriendLinksDto friendLinksDto);

    void insertFriendLinksRequest(FriendLinks friendLinks);

}
