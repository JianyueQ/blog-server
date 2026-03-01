package com.blog.business.service;

import com.blog.business.domain.dto.MessageReadStatusDto;
import com.blog.business.domain.entity.MessageRecord;
import com.blog.business.domain.vo.MessageReadStatusVo;
import com.blog.business.domain.vo.MessageRecordVo;

import java.util.List;

/**
 * @author 31373
 */
public interface MessageRecordService {
    void insertMessageRecord(MessageRecord messageRecord);

    MessageReadStatusVo updateMessageReadStatus(MessageReadStatusDto messageReadStatusDto);

    List<MessageRecordVo> getMessageRecordList();

    List<MessageRecordVo> getMessageRecordListUnread();

    List<MessageRecordVo> getMessageRecordListRead();

    int deleteMessageRecordByIds(Long[] ids);

    void cleanMessageRecord();

    int allMessageRead();

}
