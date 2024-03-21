package com.data.call.listener;

import com.data.call.record.service.RecordService;
import com.data.call.record.service.TestService;
import com.data.call.record.service.impl.RecordServiceImpl;
import org.json.JSONObject;
import org.freeswitch.esl.client.IEslEventListener;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Map;


/**
 *@author:Cai.Hongchao
 *@create: 2024-03-01 16:21
 *@Description: 事件处理监听器
 */

@Component
public class MyListener implements IEslEventListener {

    private final Logger logger =
            LoggerFactory.getLogger(MyListener.class);

    @Resource
    private RecordService recordService;

    @Override
    public void eventReceived(EslEvent eslEvent) {
        String eventName= eslEvent.getEventName();
        logger.info("eventName-->"+ eventName);
        Map<String, String> eventHeaders;
        switch (eventName) {
            case "CHANNEL_HANGUP":
                logger.info("eventReceived-->"+ new JSONObject(eslEvent.getEventHeaders()));
                recordService.insertCallRecord(eslEvent.getEventHeaders());
                break;
            case "CHANNEL_BRIDGE":
                logger.info("CHANNEL_BRIDGE");
                logger.info("eventReceived-->"+ new JSONObject(eslEvent.getEventHeaders()));
                recordService.insertBridgeRecord(eslEvent.getEventHeaders());
                break;
            case "CUSTOM":
                eventHeaders = eslEvent.getEventHeaders();
                String time = eventHeaders.get("Event-Date-Local");
                //logger.info("MSG："+msg);
                String agentCode = eventHeaders.get("user_name");
                if (eventHeaders.get("Event-Subclass").equals("sofia::register")){
                    logger.info(agentCode+ " 用户签入！   "+ time);
                    recordService.insertRegisterRecord(eventHeaders);
                } else if (eventHeaders.get("Event-Subclass").equals("sofia::unregister")){
                    logger.info(agentCode+ " 用户签出！   "+ time);
                    recordService.updateRegisterRecord(eventHeaders);
                } else if (eventHeaders.get("Event-Subclass").equals("sofia::expire")){
                    logger.info(agentCode+ " 注册超时！   "+ time);
                    recordService.updateRegisterRecord(eventHeaders);
                }
            default:
                logger.info("eventReceived-->"+ new JSONObject(eslEvent.getEventHeaders()));
                break;
        }
    }

    @Override
    public void backgroundJobResultReceived(EslEvent event) {
        logger.info("backgroundJobResultReceived-->"+ new JSONObject(event.getEventHeaders()));
    }

}
