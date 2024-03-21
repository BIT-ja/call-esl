package com.data.call.record.service.impl;

import com.data.call.record.dto.BridgeRecordDto;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
        new Thread(() -> {
            try {
                int registerCount = getRegisterRecordCountByCallId(eslEvent.get("call-id"));
                if (registerCount == 0) {
                    RegisterEventDto registerEventDto = new RegisterEventDto();
                    registerEventDto.setId(Long.parseLong(eslEvent.get("Event-Date-Timestamp")));
                    registerEventDto.setUser(Integer.parseInt(eslEvent.get("accountcode")));
                    registerEventDto.setStartTime(eslEvent.get("Event-Date-Local"));
                    registerEventDto.setCallId(eslEvent.get("call-id"));
                    registerEventDto.setContact(eslEvent.get("contact"));
                    int insertCount = recordMapper.insertRegisterRecord(registerEventDto);
                    logger.info("注册记录插入成功");
                } else {
                    logger.info("已注册");
                }
            } catch (Exception e) {
                logger.info("插入注册记录失败{}", e);
            }
        }).start();
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
    public void updateRegisterRecord(Map<String, String> eslEvent) {
        new Thread(() -> {
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

        }).start();
    }

    /**
     * 插入通话记录
     * @author:Cai.Hongchao
     * @create: 2024-03-13 09:00
     * @param eventHeaders
     * @return Map<String, Object>
     */
    @Override
    public Map<String,Object> insertCallRecord(Map<String, String> eventHeaders) {
        Map<String,Object> resultMap = new HashMap<>();
        try {
            CallRecordDto callRecordDto = new CallRecordDto();
            Long sid = SnowflakeUtil.genId();
            callRecordDto.setSid(sid);
            // 主叫号码
            callRecordDto.setCaller(eventHeaders.get("Caller-Caller-ID-Number"));
            // 被叫号码
            callRecordDto.setCallee(eventHeaders.get("Caller-Destination-Number"));
            // 呼叫方向
            if ("inbound".equals(eventHeaders.get("variable_direction"))) {
                callRecordDto.setDirection(1);
            } else if ("outbound".equals(eventHeaders.get("variable_direction"))){
                callRecordDto.setDirection(2);
            } else {
                callRecordDto.setDirection(0);
            }
            // 开始时间
            callRecordDto.setCreateTime(Stamp2Time.stamp2Time(eventHeaders.get("Caller-Profile-Created-Time")));
            // 接听时间
            callRecordDto.setAnswerTime(Stamp2Time.stamp2Time(eventHeaders.get("Caller-Channel-Answered-Time")));
            // 结束时间
            callRecordDto.setEndTime(eventHeaders.get("Event-Date-Local"));
            // uuid
            callRecordDto.setUuid(eventHeaders.get("variable_uuid"));
            // 挂机原因
            callRecordDto.setHangupCause(eventHeaders.get("Hangup-Cause"));
            // sip挂机代码
            callRecordDto.setSipHangupCause(eventHeaders.get("variable_proto_specific_hangup_cause"));
            // contact
            callRecordDto.setContact(eventHeaders.get("variable_sip_contact_host")
                    + ":"+ eventHeaders.get("variable_sip_contact_port"));
            // fs主机
            callRecordDto.setDomain(eventHeaders.get("variable_domain_name"));

            recordMapper.insertCallRecord(callRecordDto);
        } catch (Exception e) {
            logger.error("插入通话记录失败{}", e.getMessage());
            resultMap.put("code", 500);
            resultMap.put("msg", "插入通话记录失败");
            resultMap.put("data", 0);
            return resultMap;
        }
        return resultMap;
    }




    /**
     * 插入桥接记录
     *
     * @author:Cai.Hongchao
     * @create: 2024-03-21 11:00
     * @param eventHeaders esl事件数据
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> insertBridgeRecord(Map<String, String> eventHeaders) {
        try {
            BridgeRecordDto bridgeRecordDto = new BridgeRecordDto();
            CallRecordDto callRecordDto = new CallRecordDto();
            Long sid = SnowflakeUtil.genId();
            callRecordDto.setSid(sid);
            bridgeRecordDto.setBridgeTime(eventHeaders.get("Event-Date-Local"));
            bridgeRecordDto.setUuidA(eventHeaders.get("Bridge-A-Unique-ID"));
            bridgeRecordDto.setUuidB(eventHeaders.get("Bridge-B-Unique-ID"));
            recordMapper.insertBridgeRecord(bridgeRecordDto);
        } catch (Exception e) {
            logger.error("插入桥接记录失败{}", e.getMessage());
        }
        return null;
    }

    @Override
    public Map<String, Object> getRegister(Map<String, String> map) {
        Map<String,Object> resultMap = new HashMap<>();
        try {
            List<RegisterEventDto> registerEventDtoList = recordMapper.getRegisterRecord(map);
            resultMap.put("code", 200);
            resultMap.put("msg", "查询成功");
            resultMap.put("data", registerEventDtoList);
        } catch (Exception e) {
            logger.error("获取注册信息失败{}", e.getMessage());
            resultMap.put("code", 500);
            resultMap.put("msg","获取注册信息失败");
            resultMap.put("data",e);
        }
        return resultMap;
    }

}
