package com.blog.business.service.serviceImpl;

import com.blog.business.constant.BusinessCacheConstants;
import com.blog.business.domain.dto.MessageReadStatusDto;
import com.blog.business.domain.entity.MessageRecord;
import com.blog.business.domain.vo.MessageReadStatusVo;
import com.blog.business.domain.vo.MessageRecordVo;
import com.blog.business.mapper.MessageRecordMapper;
import com.blog.business.service.MessageRecordService;
import com.blog.common.utils.DateUtils;
import com.blog.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 31373
 */
@Service
public class MessageRecordServiceImpl implements MessageRecordService {

    @Autowired
    private MessageRecordMapper messageRecordMapper;

    @CacheEvict(cacheNames = {
            BusinessCacheConstants.MESSAGE_RECORD_LIST_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_UNREAD_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_READ_CACHE
    }, allEntries = true)
    @Override
    public void insertMessageRecord(MessageRecord messageRecord) {
        messageRecordMapper.insertMessageRecord(messageRecord);
    }

    @CacheEvict(cacheNames = {
            BusinessCacheConstants.MESSAGE_RECORD_LIST_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_UNREAD_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_READ_CACHE
    }, allEntries = true)
    @Override
    public MessageReadStatusVo updateMessageReadStatus(MessageReadStatusDto messageReadStatusDto) {
        MessageRecord messageRecord = new MessageRecord();
        messageRecord.setIsRead(messageReadStatusDto.getIsRead());
        messageRecord.setMessageId(messageReadStatusDto.getMessageId());
        Date nowDate = DateUtils.getNowDate();
        messageRecord.setReadTime(nowDate);
        messageRecord.setUpdateTime(nowDate);
        MessageReadStatusVo messageReadStatusVo = new MessageReadStatusVo();
        messageReadStatusVo.setMessageId(messageReadStatusDto.getMessageId());
        messageReadStatusVo.setIsRead(messageReadStatusDto.getIsRead());
        messageReadStatusVo.setReadTime(DateUtils.dateTimeNow());
        return messageRecordMapper.updateMessageReadStatus(messageRecord) > 0 ? messageReadStatusVo : null;
    }

    @Override
    public List<MessageRecordVo> getMessageRecordList() {
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        return messageRecordMapper.getMessageRecordList(adminId);
    }

    @Override
    public List<MessageRecordVo> getMessageRecordListUnread() {
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        return messageRecordMapper.getMessageRecordListUnread(adminId);
    }

    @Override
    public List<MessageRecordVo> getMessageRecordListRead() {
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        return messageRecordMapper.getMessageRecordListRead(adminId);
    }

    @CacheEvict(cacheNames = {
            BusinessCacheConstants.MESSAGE_RECORD_LIST_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_UNREAD_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_READ_CACHE
    }, allEntries = true)
    @Override
    public int deleteMessageRecordByIds(Long[] ids) {
        return messageRecordMapper.deleteMessageRecordByIds(ids);
    }

    @CacheEvict(cacheNames = {
            BusinessCacheConstants.MESSAGE_RECORD_LIST_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_UNREAD_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_READ_CACHE
    }, allEntries = true)
    @Override
    public void cleanMessageRecord() {
        messageRecordMapper.cleanMessageRecord();
    }

    @CacheEvict(cacheNames = {
            BusinessCacheConstants.MESSAGE_RECORD_LIST_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_UNREAD_CACHE,
            BusinessCacheConstants.MESSAGE_RECORD_LIST_READ_CACHE
    }, allEntries = true)
    @Override
    public int allMessageRead() {
        Long adminId = SecurityUtils.getLoginUserOnAdmin().getAdminId();
        return messageRecordMapper.allMessageRead(adminId, DateUtils.getNowDate());
    }


}
