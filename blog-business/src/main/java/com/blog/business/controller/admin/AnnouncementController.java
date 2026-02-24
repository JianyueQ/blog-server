package com.blog.business.controller.admin;

import com.blog.business.domain.dto.AnnouncementDto;
import com.blog.business.domain.vo.AnnouncementVo;
import com.blog.business.service.AnnouncementService;
import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公告计划控制器
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/announcement")
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

    /**
     * 修改公告计划
     */
    @Log(title = "公告计划", businessType = BusinessType.UPDATE)
    @PostMapping()
    public AjaxResult updateAnnouncement(@RequestBody AnnouncementDto announcementDto) {
        return toAjax(announcementService.updateAnnouncement(announcementDto));
    }
}
