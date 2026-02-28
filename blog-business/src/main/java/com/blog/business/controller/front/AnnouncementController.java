package com.blog.business.controller.front;

import com.blog.business.domain.vo.AnnouncementVo;
import com.blog.business.service.AnnouncementService;
import com.blog.common.annotation.Anonymous;
import com.blog.common.core.controller.BaseController;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告计划控制器
 *
 * @author 31373
 */
@Anonymous
@RestController("frontAnnouncementController")
@RequestMapping("/blog/announcement")
public class AnnouncementController extends BaseController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 公告计划
     */
    @GetMapping()
    public AjaxResult getAnnouncement() {
        AnnouncementVo announcementVo = announcementService.getAnnouncement();
        return AjaxResult.success(announcementVo);
    }
}
