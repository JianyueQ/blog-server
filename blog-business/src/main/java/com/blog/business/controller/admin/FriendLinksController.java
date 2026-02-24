package com.blog.business.controller.admin;

import com.blog.business.domain.dto.FriendLinksDto;
import com.blog.business.domain.vo.FriendLinksDetailVo;
import com.blog.business.domain.vo.FriendLinksListVo;
import com.blog.business.service.FriendLinksService;
import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 31373
 */
@RestController("friendLinksAdminController")
@RequestMapping("/system/friendLinks")
public class FriendLinksController extends BaseController {

    @Autowired
    private FriendLinksService friendLinksService;

    /**
     * 获取友链列表
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<FriendLinksListVo> friendLinksListVoList = friendLinksService.getFriendLinksList();
        return getDataTable(friendLinksListVoList);
    }

    /**
     * 获取友链详情(修改操作调用)
     */
    @GetMapping("/detail")
    public AjaxResult detail(@RequestParam("friendLinksId") Long friendLinksId) {
        FriendLinksDetailVo friendLinksDetailVo = friendLinksService.getFriendLinksDetail(friendLinksId);
        return AjaxResult.success(friendLinksDetailVo);
    }

    /**
     * 获取通过前台申请的友链列表
     */
    @GetMapping("/approvedList")
    public TableDataInfo approvedList() {
        startPage();
        List<FriendLinksListVo> friendLinksListVoList = friendLinksService.getApprovedFriendLinksList();
        return getDataTable(friendLinksListVoList);
    }

    /**
     * 新增友链
     */
    @Log(title = "友链管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FriendLinksDto friendLinksDto) {
        return toAjax(friendLinksService.addFriendLinks(friendLinksDto));
    }

    /**
     * 修改友链
     */
    @Log(title = "友链管理", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@RequestBody FriendLinksDto friendLinksDto) {
        return toAjax(friendLinksService.updateFriendLinks(friendLinksDto));
    }

    /**
     * 修改友链展示状态 状态(0-隐藏, 1-显示)
     */
    @Log(title = "友链管理", businessType = BusinessType.UPDATE)
    @PostMapping("/status")
    public AjaxResult status(@RequestBody FriendLinksDto friendLinksDto) {
        Long friendLinksId = friendLinksDto.getFriendLinksId();
        Integer status = friendLinksDto.getStatus();
        return toAjax(friendLinksService.updateFriendLinksStatus(friendLinksId, status));
    }

    /**
     * 删除友链
     */
    @Log(title = "友链管理", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{friendLinksId}")
    public AjaxResult delete(@PathVariable("friendLinksId") Long friendLinksId) {
        return toAjax(friendLinksService.deleteFriendLinks(friendLinksId));
    }
}
