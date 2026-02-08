package com.blog.web.controller.monitor;

import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.entity.SysLogininfor;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import com.blog.system.domain.dto.SysLogininforDto;
import com.blog.system.domain.vo.SysLogininforVo;
import com.blog.system.service.AccessRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 访问记录
 *
 * @author 31373
 */
@RestController
@RequestMapping("/monitor/access")
public class AccessRecordsController extends BaseController {

    @Autowired
    private AccessRecordsService accessRecordsService;

    /**
     * 获取访问记录列表
     * @param logininforDto 访问记录
     * @return 访问记录
     */
    @GetMapping("/list")
    public TableDataInfo list(SysLogininforDto logininforDto) {
        startPage();
        List<SysLogininforVo> list = accessRecordsService.selectAccessRecordsList(logininforDto);
        return getDataTable(list);
    }

    /**
     * 删除访问记录
     * @param infoIds 访问记录ID
     * @return 结果
     */
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable("infoIds") Long[] infoIds) {
        return toAjax(accessRecordsService.deleteAccessRecordsByIds(infoIds));
    }

    /**
     * 清空访问记录
     * @return 结果
     */
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        accessRecordsService.cleanAccessRecords();
        return success();
    }
}
