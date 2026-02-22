package com.blog.business.controller.front;

import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.domain.vo.FrontGuestbookListVo;
import com.blog.business.service.GuestbookService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 获取可以展示的留言列表
     */
    @GetMapping("/list")
    public TableDataInfo getGuestbookList() {
        startPage();
        List<FrontGuestbookListVo> frontGuestbookListVoList = guestbookService.getFrontGuestbookList();
        return getDataTable(frontGuestbookListVoList);
    }

    /**
     * 前台留言或回复
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GuestbookDto guestbookDto) {
        return toAjax(guestbookService.addMessage(guestbookDto));
    }

}
