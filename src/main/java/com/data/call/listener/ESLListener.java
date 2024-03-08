package com.data.call.listener;

import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.CommandResponse;
import org.freeswitch.esl.client.transport.SendMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 *@author:Cai.Hongchao
 *@create: 2024-03-01 16:10
 *@Description: esl监听程序
 */

@Component
public class ESLListener {
    private static String HOST;
    private static int PORT;
    private static String PASSWORD;
    private static int TIMEOUT;

    @Value("${esl.host}")
    public void setFsHost(String host){
        HOST = host;
    }

    @Value("${esl.port}")
    public void setFsPort(int port){
        PORT = port;
    }

    @Value("${esl.password}")
    public void setFsPassword(String password){
        PASSWORD = password;
    }

    @Value("${esl.timeout}")
    public void setFsTimeout(int timeout){
        TIMEOUT = timeout;
    }


    private final Logger logger =
            LoggerFactory.getLogger(ESLListener.class);

    @Resource
    private MyListener listener;

    private static Client client;

    @PostConstruct
    public void inboundFS() {
        listen();
        Thread daemonThread = new Thread(()->{
            while (true){
                try {
                    if (!client.canSend()) {
                        logger.info("can not send, reconnect");
                        listen();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("daemonThread error-->{}", e.getMessage());
                }
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.run();
    }
    public void listen(){
        try {
            client = new Client();
            client.connect(HOST, PORT, PASSWORD, TIMEOUT);
            // 不能使用new创建listener，否则
            //IEslEventListener listener = new MyListener();
            client.addEventListener(listener);
            client.setEventSubscriptions("plain", "all");
        } catch (InboundConnectionFailure e) {
            logger.error("连接失败", e);
        }
    }

    /**
    * Description: uuid执行application
    * date: 2024/3/8 9:18

    * @author: Cai.Hongchao
    * @since JDK 8
    */
    public void sendMsg(Client client, String channelUuid) {
        SendMsg msg = new SendMsg(channelUuid);
        msg.addCallCommand("execute");
        msg.addExecuteAppName("lua");
        msg.addExecuteAppArg("/usr/local/freeswitch/scripts/tts.lua");
        CommandResponse response = client.sendMessage(msg);
        System.out.println("response ：" + response.toString());
    }



}
