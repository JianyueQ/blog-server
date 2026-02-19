package com.blog.business.controller.admin;

import com.blog.business.domain.vo.FriendLinksListVo;
import com.blog.business.service.FriendLinksService;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 31373
 */
@RestController
@RequestMapping("/system/friendLinks")
public class FriendLinksController extends BaseController {

    @Autowired
    private FriendLinksService friendLinksService;

    /**
     * 获取站点列表
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<FriendLinksListVo> friendLinksListVoList = friendLinksService.getFriendLinksList();
        return getDataTable(friendLinksListVoList);
    }


}
