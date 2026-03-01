package com.blog.web.controller.monitor;

import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.SystemUptimeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *系统累计运行时长监控控制器
 * 提供累计运行时长查询功能
 * 
 * @author 31373
 */
@RestController
@RequestMapping("/monitor/uptime")
public class SystemUptimeController {
    /**
     * 获取格式化的累计运行时长
     * 
     * @return 格式化的累计运行时长
     */
    @GetMapping("/formatted")
    public AjaxResult getFormattedTotalUptime() {
        return AjaxResult.success(SystemUptimeUtils.getTotalUptime());
    }
}