package com.blog.business.controller.front;

import com.blog.common.annotation.Anonymous;
import com.blog.common.core.controller.BaseController;
import com.blog.common.domain.AjaxResult;
import com.blog.common.utils.SystemUptimeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *系统累计运行时长监控控制器
 * 提供累计运行时长查询功能
 * 
 * @author 31373
 */
@Anonymous
@RestController("FrontSystemUptimeController")
@RequestMapping("/blog/monitor/uptime")
public class SystemUptimeController extends BaseController {
    /**
     * 获取格式化的累计运行时长
     * 
     * @return 格式化的累计运行时长
     */
    @GetMapping("/formatted")
    public AjaxResult getFormattedTotalUptime() {
        Object string = SystemUptimeUtils.formatDuration(SystemUptimeUtils.getTotalUptime());
        return AjaxResult.success(string);
    }
}