package com.blog.business.mapper;

import com.blog.business.domain.entity.FriendLinks;
import com.blog.business.domain.vo.DisplayedFriendLinksListVo;
import com.blog.business.domain.vo.FriendLinksDetailVo;
import com.blog.business.domain.vo.FriendLinksListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface FriendLinksMapper {
    List<FriendLinksListVo> getFriendLinksList();

    FriendLinksDetailVo getFriendLinksDetail(@Param("friendLinksId") Long friendLinksId);

    List<FriendLinksListVo> getApprovedFriendLinksList();

    int addFriendLinks(FriendLinks friendLinks);

    int updateFriendLinks(FriendLinks friendLinks);

    int updateFriendLinksStatus(@Param("friendLinksId") Long friendLinksId,@Param("status") Integer status);

    int deleteFriendLinks(@Param("friendLinksId") Long friendLinksId);

    List<DisplayedFriendLinksListVo> getDisplayedFriendLinksList();

}
