package com.blog.web.controller.monitor;

import com.blog.common.domain.AjaxResult;
import com.blog.framework.web.domain.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author 31373
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    /**
     * 获取服务器监控信息
     * @return 服务器监控信息
     * @throws Exception 如果获取服务器信息失败，则抛出异常
     */
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }

}
