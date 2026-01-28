package com.blog.web.controller.system;

import com.blog.common.constant.Constants;
import com.blog.common.domain.AjaxResult;
import com.blog.framework.web.service.SysLoginService;
import com.blog.common.core.domain.model.LoginBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 * @author 31373
 */
@RestController
public class SysLoginController {

    private static final Logger log = LoggerFactory.getLogger(SysLoginController.class);
    private final SysLoginService sysLoginService;

    public SysLoginController(SysLoginService sysLoginService) {
        this.sysLoginService = sysLoginService;
    }

    @PostMapping("login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        log.info("login in {},userType in {}", loginBody.getUsername(),loginBody.getUserType());
        AjaxResult ajax = AjaxResult.success();
        String token = sysLoginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid(), loginBody.getUserType());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


}
