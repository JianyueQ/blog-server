package com.blog.business.service.serviceImpl;

import com.blog.business.domain.vo.FriendLinksListVo;
import com.blog.business.mapper.FriendLinksMapper;
import com.blog.business.service.FriendLinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31373
 */
@Service
public class FriendLinksServiceImpl implements FriendLinksService {

    @Autowired
    private FriendLinksMapper friendLinksMapper;


    @Override
    public List<FriendLinksListVo> getFriendLinksList() {
        return List.of();
    }
}
