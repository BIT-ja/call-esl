package com.data.call.record.service.impl;

import com.data.call.record.mapper.RecordMapper;
import com.data.call.record.dto.RegisterEventDto;
import com.data.call.record.service.RecordService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *@author:Cai.Hongchao
 *@create: 2024-03-01 16:48
 *@Description: 事件记录
 */

@Service
@Slf4j
@Data
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordMapper recordMapper;

    private final Logger logger =
            LoggerFactory.getLogger(RecordServiceImpl.class);

    @Override
    public void insertRecordData(Map<String, String> EslEvent) {
        recordMapper.insertRecordData(null);
    }

    @Override
    public Map<String, Object> insertRegisterRecord(Map<String, String> eslEvent) {
        Map<String, Object> res = new HashMap<>();
        try {
            int registerCount = getRegisterRecordCountByCallId(eslEvent.get("call-id"));
            if (registerCount == 0) {
                RegisterEventDto registerEventDto = new RegisterEventDto();
                registerEventDto.setId(Long.parseLong(eslEvent.get("Event-Date-Timestamp")));
                registerEventDto.setUserNo(Integer.parseInt(eslEvent.get("accountcode")));
                registerEventDto.setStartTime(eslEvent.get("Event-Date-Local"));
                registerEventDto.setCallId(eslEvent.get("call-id"));
                registerEventDto.setContact(eslEvent.get("contact"));
                int insertCount = recordMapper.insertRegisterRecord(registerEventDto);
                res.put("code", 200);
                res.put("msg", "注册记录插入成功");
                res.put("data", insertCount);
            } else {
                res.put("code", 200);
                res.put("msg", "已注册");
                res.put("data", 0);
            }
            return res;
        } catch (Exception e) {
            logger.info("插入注册记录失败{}", e.getMessage());
            res.put("code", 500);
            res.put("msg", "插入注册记录失败");
            res.put("data", 0);
            return res;
        }
    }

    @Override
    public int getRegisterRecordCountByCallId(String callId) {
        return recordMapper.getRegisterRecordCountByCallId(callId);
    }

    @Override
    public void updateRegisterRecord(Map<String, String> eslEvent) {
        RegisterEventDto registerEventDto = new RegisterEventDto();
        registerEventDto.setCallId(eslEvent.get("call-id"));
        registerEventDto.setEndTime(eslEvent.get("Event-Date-Local"));
        if (eslEvent.get("Event-Subclass").equals("sofia::unregister")) {
            registerEventDto.setEndType(0);
        } else {
            registerEventDto.setEndType(1);
        }
        recordMapper.updateRegisterRecord(registerEventDto);
    }
}
