package com.blog.business.controller.admin;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.dto.GuestbookDto;
import com.blog.business.domain.dto.GuestbookListDto;
import com.blog.business.domain.dto.GuestbookStatusDto;
import com.blog.business.domain.vo.GuestbookListVo;
import com.blog.business.service.GuestbookService;
import com.blog.common.annotation.Log;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import com.blog.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 留言板控制器
 * @author 31373
 */
@RestController("adminGuestbookController")
@RequestMapping("/system/guestbook")
public class GuestbookController extends BaseController {

    @Autowired
    private GuestbookService guestbookService;

    private static final Logger log = LoggerFactory.getLogger(GuestbookController.class);

    /**
     * 获取留言列表
     */
    @Cacheable(cacheNames = BusinessCacheConstants.GUESTBOOK_LIST_CACHE, keyGenerator = "CacheKeyGenerator")
    @GetMapping("/list")
    public TableDataInfo getGuestbookList(GuestbookListDto guestbookListDto){
        startPage();
        List<GuestbookListVo> guestbookListVoList = guestbookService.getGuestbookList(guestbookListDto);
        return getDataTable(guestbookListVoList);
    }

    /**
     * 后台用户回复留言
     */
    @Log(title = "后台用户回复留言", businessType = BusinessType.UPDATE)
    @PostMapping("/adminReplyMessage")
    public AjaxResult adminReplyMessage(@RequestBody GuestbookDto guestbookDto){
        return toAjax(guestbookService.adminReplyMessage(guestbookDto));
    }

    /**
     * 修改留言的状态
     */
    @Log(title = "修改留言状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStatus")
    public AjaxResult updateGuestbookMessageStatus(@RequestBody GuestbookStatusDto guestbookStatusDto){
        return toAjax(guestbookService.updateGuestbookMessageStatus(guestbookStatusDto));
    }

    /**
     * 删除留言
     */
    @Log(title = "删除留言", businessType = BusinessType.DELETE)
    @PostMapping("/delete/{id}")
    public AjaxResult deleteGuestbookMessage(@PathVariable("id") Long id){
        return toAjax(guestbookService.deleteGuestbookMessage(id));
    }

}
