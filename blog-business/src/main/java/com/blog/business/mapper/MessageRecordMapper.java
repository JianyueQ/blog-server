package com.blog.business.mapper;

import com.blog.business.domain.entity.MessageRecord;
import com.blog.business.domain.vo.MessageRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 31373
 */
@Mapper
public interface MessageRecordMapper {


    void insertMessageRecord(MessageRecord messageRecord);

    int updateMessageReadStatus(MessageRecord messageRecord);

    List<MessageRecordVo> getMessageRecordList(@Param("adminId") Long adminId);

    List<MessageRecordVo> getMessageRecordListUnread(Long adminId);

    List<MessageRecordVo> getMessageRecordListRead(Long adminId);

    int deleteMessageRecordByIds(@Param("ids") Long[] ids);

    void cleanMessageRecord();

}
