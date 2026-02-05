package com.blog.web.controller.monitor;

import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.entity.SysOperLog;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import com.blog.system.service.SysOperLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author 31373
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {

    private final SysOperLogService operLogService;

    public SysOperlogController(SysOperLogService operLogService) {
        this.operLogService = operLogService;
    }

    /**
     * 获取操作日志记录列表
     *
     * @param operLog 操作日志信息
     * @return 结果
     */
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    /**
     * 删除操作日志
     *
     * @param operIds 日志ID
     * @return 结果
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable("operIds") Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    /**
     * 清空操作日志
     *
     * @return 结果
     */
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return success();
    }

    /**
     * 查看日志详情
     */
    @GetMapping("/detail/{operId}")
    public AjaxResult detail(@PathVariable("operId") Long operId) {
        SysOperLog operLog = operLogService.selectOperLogById(operId);
        return AjaxResult.success(operLog);
    }

}
