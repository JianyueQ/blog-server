package com.blog.business.controller.front;

import com.blog.business.domain.dto.FrontGuestbookListDto;
import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.service.GuestbookService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.constant.HttpStatus;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 留言板控制器
 *
 * @author 31373
 */
@Anonymous
@RestController("frontGuestbookController")
@RequestMapping("/blog/user/guestbook")
public class GuestbookController extends BaseController {

    @Autowired
    private GuestbookService guestbookService;

    /**
     * 获取可以展示的根留言列表
     */
    @GetMapping("/list/isRoot")
    public AjaxResult getGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        return guestbookService.getFrontRootGuestbookList(frontGuestbookListDto);
    }

    /**
     * 获取可以展示的子留言列表
     */
    @GetMapping("/list/child")
    public AjaxResult getChildGuestbookList(FrontGuestbookListDto frontGuestbookListDto) {
        return guestbookService.getFrontChildGuestbookList(frontGuestbookListDto);
    }

    /**
     * 前台留言或回复
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GuestbookDto guestbookDto) {

        return toAjax(guestbookService.addMessage(guestbookDto));
    }

}
