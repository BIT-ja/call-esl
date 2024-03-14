package com.data.call.record.service.impl;

import com.data.call.record.dto.CallRecordDto;
import com.data.call.record.mapper.RecordMapper;
import com.data.call.record.dto.RegisterEventDto;
import com.data.call.record.service.RecordService;
import com.data.call.util.SnowflakeUtil;
import com.data.call.util.Stamp2Time;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    public void insertRecordData(Map<String, String> eslEvent) {
        recordMapper.insertRecordData(null);
    }

    /**
     * 插入注册记录
     *
     * @param eslEvent
     * @author:Cai.Hongchao
     * @create: 2024-03-13 08:48
     */
    @Override
    public void insertRegisterRecord(Map<String, String> eslEvent) {
        try {
            int registerCount = getRegisterRecordCountByCallId(eslEvent.get("call-id"));
            if (registerCount == 0) {
                RegisterEventDto registerEventDto = new RegisterEventDto();
                registerEventDto.setId(Long.parseLong(eslEvent.get("Event-Date-Timestamp")));
                registerEventDto.setUserNo(Integer.parseInt(eslEvent.get("accountcode")));
                registerEventDto.setStartTime(eslEvent.get("Event-Date-Local"));
                registerEventDto.setCallId(eslEvent.get("call-id"));
                registerEventDto.setContact(eslEvent.get("contact"));
                recordMapper.insertRegisterRecord(registerEventDto);
                logger.info("注册记录插入成功");
            } else {
                logger.info("已注册");
            }
        } catch (Exception e) {
            logger.info("插入注册记录失败{}", e);
        }
    }

    /**
     * 查询注册记录
     * @author:Cai.Hongchao
     * @create: 2024-03-13 08:55
     * @param callId
     * @return int
     */
    @Override
    public int getRegisterRecordCountByCallId(String callId) {
        return recordMapper.getRegisterRecordCountByCallId(callId);
    }

    /**
     * 更新注册记录
     * @author:Cai.Hongchao
     * @create: 2024-03-13 08:58
     * @param eslEvent
     */
    @Override
    @Async
    public void updateRegisterRecord(Map<String, String> eslEvent) {
        try {
            RegisterEventDto registerEventDto = new RegisterEventDto();
            registerEventDto.setCallId(eslEvent.get("call-id"));
            registerEventDto.setEndTime(eslEvent.get("Event-Date-Local"));
            if ("sofia::unregister".equals(eslEvent.get("Event-Subclass"))) {
                // 正常取消注册
                registerEventDto.setEndType(0);
            } else {
                // 超时或异常断开
                registerEventDto.setEndType(1);
            }
            recordMapper.updateRegisterRecord(registerEventDto);
        } catch (Exception e) {
            logger.info("更新注册记录失败{}", e);
        }
    }

    /**
     * 插入通话记录
     *
     * @param eventHeaders
     * @author:Cai.Hongchao
     * @create: 2024-03-13 09:00
     *
     * @param eventHeaders esl信息
     * @return void
     */
    @Override
    @Async
    public void insertCallRecord(Map<String, String> eventHeaders) {
        try {
            CallRecordDto callRecordDto = new CallRecordDto();
            Long sid = SnowflakeUtil.genId();
            callRecordDto.setSid(sid);
            callRecordDto.setCaller(eventHeaders.get("Caller-Caller-ID-Number"));
            callRecordDto.setCallee(eventHeaders.get("Caller-Destination-Number"));
            callRecordDto.setDirection(eventHeaders.get("Caller-Logical-Direction"));
            callRecordDto.setCreateTime(Stamp2Time.stamp2Time(eventHeaders.get("Caller-Channel-Created-Time")));
            if (!"0".equals(eventHeaders.get("Caller-Channel-Answered-Time"))){
                // 应答时间不为空
                callRecordDto.setAnswerTime(Stamp2Time.stamp2Time(eventHeaders.get("Caller-Channel-Answered-Time")));
            }
            callRecordDto.setEndTime(eventHeaders.get("Event-Date-Local"));
            callRecordDto.setUuid(eventHeaders.get("Unique-ID"));
            recordMapper.insertCallRecord(eventHeaders);
        } catch (Exception e) {
            logger.error("插入通话记录失败{}", e.getMessage());
        }
    }

    /**
     * 插入通话记录
     *
     * @param eventHeaders
     * @author:Cai.Hongchao
     * @create: 2024-03-13 11:13
     *
     * @param eventHeaders esl信息
     * @return void
     */
    @Override
    @Async
    public void insertBridgeRecord(Map<String, String> eventHeaders) {
        try {
            CallRecordDto callRecordDto = new CallRecordDto();
            Long sid = SnowflakeUtil.genId();
            callRecordDto.setSid(sid);
            callRecordDto.setUuid(eventHeaders.get("Unique-ID"));
            callRecordDto.setBLegUuid(eventHeaders.get("Other-Leg-Unique-ID"));
            callRecordDto.setBridgeTime(eventHeaders.get("variable_Event-Date-Local"));
            recordMapper.insertBridgeRecord(callRecordDto);
        } catch (Exception e){
            logger.error("插入桥接记录失败{}", e);
        }
    }

}
