package com.blog.business.service.serviceImpl;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.constant.GuestbookConstants;
import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.entity.Guestbook;
import com.blog.business.domain.vo.FrontGuestbookListVo;
import com.blog.business.domain.vo.GuestbookListVo;
import com.blog.business.mapper.GuestbookMapper;
import com.blog.business.service.GuestbookService;
import com.blog.common.core.domain.entity.Administrators;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.SecurityUtils;
import com.blog.common.utils.StringUtils;
import com.blog.common.utils.ip.AddressUtils;
import com.blog.common.utils.ip.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author 31373
 */
@Service
public class GuestbookServiceImpl implements GuestbookService {

    @Autowired
    private GuestbookMapper guestbookMapper;

    @Cacheable(cacheNames = BusinessCacheConstants.GUESTBOOK_LIST_CACHE, keyGenerator = "CacheKeyGenerator")
    @Override
    public List<GuestbookListVo> getGuestbookList(GuestbookListDto guestbookListDto) {
        List<GuestbookListVo> guestbookListVoList = guestbookMapper.getGuestbookList(guestbookListDto);
        //对子留言进行排序
        guestbookListVoList.forEach(item -> item.getReplyList().sort(Comparator.comparing(GuestbookListVo::getMessageTime)));
        return guestbookListVoList;
    }

    @CacheEvict(cacheNames = BusinessCacheConstants.GUESTBOOK_LIST_CACHE, allEntries = true)
    @Override
    public int addMessage(GuestbookDto guestbookDto) {
        Guestbook guestbook = new Guestbook();
        guestbook.setNickname(guestbookDto.getNickname());
        guestbook.setEmail(guestbookDto.getEmail());
        guestbook.setContent(guestbookDto.getContent());
        guestbook.setAvatar(guestbookDto.getAvatar());
        Long rootId = guestbookDto.getRootId();
        guestbook.setRootId(rootId);
        if (GuestbookConstants.ROOT_ID.equals(rootId)) {
            guestbook.setIsRoot(GuestbookConstants.IS_ROOT);
        } else {
            guestbook.setIsRoot(GuestbookConstants.NOT_ROOT);
        }
        if (StringUtils.isNotNull(guestbookDto.getParentId())) {
            guestbook.setParentId(guestbookDto.getParentId());
        }
        guestbook.setCreateTime(DateUtils.getNowDate());
        String ipAddr = IpUtils.getIpAddr();
        guestbook.setCreateBy(ipAddr);
        guestbook.setLocation(AddressUtils.getRealAddressByIP(ipAddr));
        return guestbookMapper.addMessage(guestbook);
    }

    @CacheEvict(cacheNames = BusinessCacheConstants.GUESTBOOK_LIST_CACHE, allEntries = true)
    @Override
    public int adminReplyMessage(GuestbookDto guestbookDto) {
        Guestbook guestbook = new Guestbook();
        Administrators administrators = SecurityUtils.getLoginUserOnAdmin().getAdministrators();
        guestbook.setNickname(administrators.getNickname());
        guestbook.setEmail(administrators.getEmail());
        guestbook.setAvatar(administrators.getAvatar());
        guestbook.setContent(guestbookDto.getContent());
        guestbook.setRootId(guestbookDto.getRootId());
        guestbook.setIsRoot(GuestbookConstants.NOT_ROOT);
        guestbook.setParentId(guestbookDto.getParentId());
        guestbook.setCreateTime(DateUtils.getNowDate());
        guestbook.setCreateBy(String.valueOf(administrators.getAdminId()));
        guestbook.setLocation(AddressUtils.getRealAddressByIP(IpUtils.getIpAddr()));
        return guestbookMapper.addMessage(guestbook);
    }

    @CacheEvict(cacheNames = BusinessCacheConstants.GUESTBOOK_LIST_CACHE, allEntries = true)
    @Override
    public int updateGuestbookMessageStatus(GuestbookStatusDto guestbookStatusDto) {
        return guestbookMapper.updateGuestbookMessageStatus(guestbookStatusDto);
    }

    @CacheEvict(cacheNames = BusinessCacheConstants.GUESTBOOK_LIST_CACHE, allEntries = true)
    @Override
    public int deleteGuestbookMessage(Long id) {
        //查询留言信息以及子留言信息
        Guestbook guestbook = guestbookMapper.getGuestbookMessageById(id);
        //如果是根留言,将一起把子留言也删除
        if (GuestbookConstants.IS_ROOT.equals(guestbook.getIsRoot())) {
            //将根留言id和子留言id存储到数组中
            List<Long> ids = new ArrayList<>();
            ids.add(id);
            if (guestbook.getReplyList() != null && !guestbook.getReplyList().isEmpty()) {
                ids.addAll(guestbook.getReplyList().stream().map(Guestbook::getGuestbookId).toList());
            }
            return guestbookMapper.deleteGuestbookMessageByIds(ids);
        }
        return guestbookMapper.deleteGuestbookMessage(id);
    }

    @Override
    public List<FrontGuestbookListVo> getFrontGuestbookList() {
        List<FrontGuestbookListVo> guestbookListVoList = guestbookMapper.getFrontGuestbookList();
        //分离根留言和非根留言
        List<FrontGuestbookListVo> rootGuestbookListVoList = guestbookListVoList.stream()
                //筛选出根留言
                .filter(item -> item.getIsRoot() != null && GuestbookConstants.IS_ROOT.equals(item.getIsRoot()))
                //初始化回复列表
                .peek(item -> item.setReplyList(new ArrayList<>()))
                .toList();
        guestbookListVoList.stream()
                .filter(item -> item.getIsRoot() != null && GuestbookConstants.NOT_ROOT.equals(item.getIsRoot()))
                //按时间正序排序，旧的留言在前，新的留言在后
                .sorted(Comparator.comparing(FrontGuestbookListVo::getMessageTime))
                .forEach(reply -> {
                    rootGuestbookListVoList.stream()
                            .filter(rootItem -> rootItem.getGuestbookId().equals(reply.getRootId()))
                            .findFirst()
                            .ifPresent(rootItem -> {
                                if (rootItem.getReplyList() == null) {
                                    rootItem.setReplyList(new ArrayList<>());
                                }
                                rootItem.getReplyList().add(reply);
                            });
                });
        return rootGuestbookListVoList;
    }
}
