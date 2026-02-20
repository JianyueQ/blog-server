package com.blog.business.controller.front;

import com.blog.business.domain.dto.FriendLinksDto;
import com.blog.business.domain.vo.DisplayedFriendLinksListVo;
import com.blog.business.domain.vo.FriendLinksDetailVo;
import com.blog.business.domain.vo.FriendLinksListVo;
import com.blog.business.service.FriendLinksService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 31373
 */
@Anonymous
@RestController("friendLinksController")
@RequestMapping("/blog/user/friendLinks")
public class FriendLinksController extends BaseController {

    @Autowired
    private FriendLinksService friendLinksService;

    /**
     * 获取可以在前台展示友链列表
     */
    @GetMapping("/getDisplayedFriendLinks")
    public TableDataInfo getDisplayedFriendLinks() {
        startPage();
        List<DisplayedFriendLinksListVo> displayedFriendLinksListVo = friendLinksService.getDisplayedFriendLinksList();
        return getDataTable(displayedFriendLinksListVo);
    }

    /**
     * 通过前台申请添加友链信息(异步操作)
     */
    @PostMapping("/requestToAdd")
    public AjaxResult requestToAdd(FriendLinksDto friendLinksDto) {
        return toAjax(friendLinksService.requestToAddFriendLinks(friendLinksDto));
    }


}
