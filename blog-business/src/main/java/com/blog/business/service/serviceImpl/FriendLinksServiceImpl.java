package com.blog.business.service.serviceImpl;

import com.blog.business.constant.FriendLinksConstant;
import com.blog.business.domain.dto.FriendLinksDto;
import com.blog.business.domain.entity.FriendLinks;
import com.blog.business.domain.vo.DisplayedFriendLinksListVo;
import com.blog.business.domain.vo.FriendLinksDetailVo;
import com.blog.business.domain.vo.FriendLinksListVo;
import com.blog.business.mapper.FriendLinksMapper;
import com.blog.business.rabbitmq.RabbitManager;
import com.blog.business.service.FriendLinksService;
import com.blog.common.constant.UserConstants;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31373
 */
@Service
public class FriendLinksServiceImpl implements FriendLinksService {

    @Autowired
    private FriendLinksMapper friendLinksMapper;
    @Autowired
    private RabbitManager rabbitManager;

    @Override
    @Cacheable(cacheNames = "friendLinks", key = "'friendLinksList'")
    public List<FriendLinksListVo> getFriendLinksList() {
        return friendLinksMapper.getFriendLinksList();
    }

    @Override
    @Cacheable(cacheNames = "friendLinks", key = "'friendLinksDetail_' + #friendLinksId")
    public FriendLinksDetailVo getFriendLinksDetail(Long friendLinksId) {
        return friendLinksMapper.getFriendLinksDetail(friendLinksId);
    }

    @Override
    @Cacheable(cacheNames = "friendLinks", key = "'approvedFriendLinksList'")
    public List<FriendLinksListVo> getApprovedFriendLinksList() {
        return friendLinksMapper.getApprovedFriendLinksList();
    }

    @Override
    @CacheEvict(cacheNames = "friendLinks", allEntries = true)
    public int addFriendLinks(FriendLinksDto friendLinksDto) {
        FriendLinks friendLinks = new FriendLinks();
        friendLinks.setName(friendLinksDto.getName());
        friendLinks.setUrl(friendLinksDto.getUrl());
        friendLinks.setLogo(friendLinksDto.getLogo());
        friendLinks.setDescription(friendLinksDto.getDescription());
        friendLinks.setEmail(friendLinksDto.getEmail());
        //设置隐藏状态
        friendLinks.setStatus(FriendLinksConstant.HIDDEN);
        friendLinks.setJoinTime(DateUtils.getTime());
        friendLinks.setCreateTime(DateUtils.getNowDate());
        friendLinks.setCreateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
        return friendLinksMapper.addFriendLinks(friendLinks);
    }

    @Override
    @CacheEvict(cacheNames = "friendLinks", allEntries = true)
    public int updateFriendLinks(FriendLinksDto friendLinksDto) {
        FriendLinks friendLinks = new FriendLinks();
        friendLinks.setFriendLinksId(friendLinksDto.getFriendLinksId());
        friendLinks.setName(friendLinksDto.getName());
        friendLinks.setUrl(friendLinksDto.getUrl());
        friendLinks.setLogo(friendLinksDto.getLogo());
        friendLinks.setDescription(friendLinksDto.getDescription());
        friendLinks.setEmail(friendLinksDto.getEmail());
        friendLinks.setUpdateTime(DateUtils.getNowDate());
        friendLinks.setUpdateBy(String.valueOf(SecurityUtils.getLoginUserOnAdmin().getAdminId()));
        return friendLinksMapper.updateFriendLinks(friendLinks);
    }

    @Override
    @CacheEvict(cacheNames = "friendLinks", allEntries = true)
    public int updateFriendLinksStatus(Long friendLinksId, Integer status) {
        return friendLinksMapper.updateFriendLinksStatus(friendLinksId, status);
    }

    @Override
    @CacheEvict(cacheNames = "friendLinks", allEntries = true)
    public int deleteFriendLinks(Long friendLinksId) {
        return friendLinksMapper.deleteFriendLinks(friendLinksId);
    }

    @Override
    @Cacheable(cacheNames = "friendLinks", key = "'displayedFriendLinksList'")
    public List<DisplayedFriendLinksListVo> getDisplayedFriendLinksList() {
        return friendLinksMapper.getDisplayedFriendLinksList();
    }

    @Override
    @CacheEvict(cacheNames = "friendLinks", allEntries = true)
    public int requestToAddFriendLinks(FriendLinksDto friendLinksDto) {
        return rabbitManager.sendFriendLinksRequest(friendLinksDto);
    }

    @Override
    @CacheEvict(cacheNames = "friendLinks", allEntries = true)
    public void insertFriendLinksRequest(FriendLinks friendLinks) {
        friendLinks.setCreateTime(DateUtils.getNowDate());
        friendLinks.setJoinTime(DateUtils.getTime());
        friendLinksMapper.addFriendLinks(friendLinks);
    }
}
