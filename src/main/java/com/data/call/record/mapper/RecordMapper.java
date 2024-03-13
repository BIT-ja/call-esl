package com.data.call.record.mapper;

import com.data.call.record.dto.EventDto;
import com.data.call.record.dto.RegisterEventDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 *@author:Cai.Hongchao
 *@create: 2024-03-01 16:59
 *@Description: 记录持久化
 */

@Mapper
public interface RecordMapper {

    void insertRecordData(EventDto eventDto);

    int insertRegisterRecord(RegisterEventDto eventDto);

    void updateRegisterRecord(RegisterEventDto registerEventDto);

    int getRegisterRecordCountByCallId(String callId);

    void insertCallRecord(Map<String, String> eventHeaders);
}
