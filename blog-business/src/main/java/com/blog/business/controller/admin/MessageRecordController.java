package com.blog.business.controller.admin;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.dto.MessageReadStatusDto;
import com.blog.business.domain.vo.MessageReadStatusVo;
import com.blog.business.domain.vo.MessageRecordVo;
import com.blog.business.service.MessageRecordService;
import com.blog.common.core.controller.BaseController;
import com.blog.common.core.page.TableDataInfo;
import com.blog.common.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息记录控制器
 *
 * @author 31373
 */
@RestController
@RequestMapping("/system/messageRecord")
public class MessageRecordController extends BaseController {

    @Autowired
    private MessageRecordService messageRecordService;

    /**
     * 获取消息列表
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = BusinessCacheConstants.MESSAGE_RECORD_LIST_CACHE, keyGenerator = "CacheKeyGenerator")
    public TableDataInfo list() {
        startPage();
        List<MessageRecordVo> messageRecordVoList = messageRecordService.getMessageRecordList();
        return getDataTable(messageRecordVoList);
    }

    /**
     * 修改消息阅读状态
     */
    @PostMapping("/updateMessageReadStatus")
    public AjaxResult updateMessageReadStatus(@RequestBody MessageReadStatusDto messageReadStatusDto) {
        MessageReadStatusVo messageReadStatusVo = messageRecordService.updateMessageReadStatus(messageReadStatusDto);
        return messageReadStatusVo != null ? AjaxResult.success(messageReadStatusVo) : AjaxResult.error();
    }

    /**
     * 获取未读消息列表
     */
    @Cacheable(cacheNames = BusinessCacheConstants.MESSAGE_RECORD_LIST_UNREAD_CACHE, keyGenerator = "CacheKeyGenerator")
    @GetMapping("/listUnread")
    public TableDataInfo listUnread() {
        startPage();
        List<MessageRecordVo> messageRecordVoList = messageRecordService.getMessageRecordListUnread();
        return getDataTable(messageRecordVoList);
    }

    /**
     * 获取已读消息列表
     */
    @Cacheable(cacheNames = BusinessCacheConstants.MESSAGE_RECORD_LIST_READ_CACHE, keyGenerator = "CacheKeyGenerator")
    @GetMapping("/listRead")
    public TableDataInfo listRead() {
        startPage();
        List<MessageRecordVo> messageRecordVoList = messageRecordService.getMessageRecordListRead();
        return getDataTable(messageRecordVoList);
    }

    /**
     * 删除消息
     */
    @PostMapping("/delete/{ids}")
    public AjaxResult delete(@PathVariable("ids") Long[] ids) {
        return toAjax(messageRecordService.deleteMessageRecordByIds(ids));
    }

    /**
     * 清空消息
     */
    @PostMapping("/clean")
    public AjaxResult clean() {
        messageRecordService.cleanMessageRecord();
        return AjaxResult.success();
    }

}
