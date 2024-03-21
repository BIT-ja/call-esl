package com.data.call.record.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author:Cai.Hongchao
 * @create: 2024-03-01 16:47
 * @Description: 事件记录
 */

@Service
public interface RecordService {
    void insertRecordData(Map<String,String> eslEvent);

    void insertRegisterRecord(Map<String,String> eslEvent);

    int getRegisterRecordCountByCallId(String callId);

    void updateRegisterRecord(Map<String,String> eslEvent);

    Map<String,Object> insertCallRecord(Map<String, String> eventHeaders);

    Map<String, Object> getRegister(Map<String, String> map);

    Map<String,Object> insertBridgeRecord(Map<String, String> eventHeaders);
}
