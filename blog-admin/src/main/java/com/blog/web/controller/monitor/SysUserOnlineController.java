package com.blog.web.controller.monitor;

import com.blog.common.annotation.Log;
import com.blog.common.constant.CacheConstants;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.domain.model.LoginUserOnAdmin;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.core.redis.RedisCache;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import com.blog.common.utils.StringUtils;
import com.blog.system.domain.vo.SysUserOnlineVo;
import com.blog.system.service.SysUserOnlineService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author 31373
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {

    private final SysUserOnlineService userOnlineService;
    private final RedisCache redisCache;

    public SysUserOnlineController(SysUserOnlineService userOnlineService, RedisCache redisCache) {
        this.userOnlineService = userOnlineService;
        this.redisCache = redisCache;
    }


    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "ipaddr", required = false) String ipaddr, @RequestParam(value = "userName", required = false) String userName) {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnlineVo> userOnlineList = new ArrayList<SysUserOnlineVo>();
        for (String key : keys) {
            LoginUserOnAdmin user = redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
            } else if (StringUtils.isNotEmpty(ipaddr)) {
                userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
            } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getAdministrators())) {
                userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
            } else {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable("tokenId") String tokenId) {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return success();
    }
}
