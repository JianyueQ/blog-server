package com.blog.business.controller.admin;

import com.blog.business.domain.dto.VisitorRecordDto;
import com.blog.business.domain.dto.VisitorRecordListDto;
import com.blog.business.domain.vo.VisitorRecordDetailVo;
import com.blog.business.domain.vo.VisitorRecordVo;
import com.blog.business.service.VisitorRecordService;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访客记录控制器
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/visitor/record")
public class VisitorRecordController extends BaseController {

    @Autowired
    private VisitorRecordService visitorRecordService;

    /**
     * 获取访客记录列表
     */
    @GetMapping("/list")
    public TableDataInfo getVisitorRecordList(VisitorRecordListDto visitorRecordListDto) {
        startPage();
        List<VisitorRecordVo> visitorRecordVoList = visitorRecordService.getVisitorRecordList(visitorRecordListDto);
        return getDataTable(visitorRecordVoList);
    }

    /**
     * 获取访客记录详情
     */
    @GetMapping("/detail")
    public AjaxResult getVisitorRecordDetail(@RequestParam("visitorRecordId") String visitorRecordId) {
        VisitorRecordDetailVo visitorRecordDetail = visitorRecordService.getVisitorRecordDetail(visitorRecordId);
        return AjaxResult.success(visitorRecordDetail);
    }

    /**
     * 更新访客记录黑名单状态
     *
     * @return 结果
     */
    @PostMapping("/update/blacklist")
    public AjaxResult updateBlacklist(@RequestBody VisitorRecordDto visitorRecordDto) {
        return toAjax(visitorRecordService.updateBlacklist(visitorRecordDto));
    }

    /**
     * 清空访客记录
     */
    @PostMapping("/clean")
    public AjaxResult clean() {
        visitorRecordService.cleanVisitorRecord();
        return success();
    }

}
