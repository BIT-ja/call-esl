package com.data.call.record.mapper;

import com.data.call.record.dto.BridgeRecordDto;
import com.data.call.record.dto.CallRecordDto;
import com.data.call.record.dto.EventDto;
import com.data.call.record.dto.RegisterEventDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
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

    int insertCallRecord(CallRecordDto callRecordDto);

    List<RegisterEventDto> getRegisterRecord(Map<String, String> map);

    int insertBridgeRecord(BridgeRecordDto bridgeRecordDto);

    List<CallRecordDto> getCallRecord(Map<String, String> map);

    List<BridgeRecordDto> getBridgeRecord(Map<String, String> map);
}
