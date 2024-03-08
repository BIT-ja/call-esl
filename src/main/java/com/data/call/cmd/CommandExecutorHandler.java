package com.data.call.cmd;

import org.freeswitch.esl.client.inbound.Client;
import org.freeswitch.esl.client.inbound.InboundConnectionFailure;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author:Cai.Hongchao
 * @create: 2024-03-07 17:21
 * @Description: 命令执行
 */
@Component
public class CommandExecutorHandler {
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

    private static Client client;

    private static final Logger logger =
            LoggerFactory.getLogger(CommandExecutorHandler.class);

    /**
    * Description: 测试eslClient是否已连接
    * date: 2024/3/8 9:35

    * @author: Cai.Hongchao
    * @since JDK 11
    */
    private static void connect(){
        if(client == null){
            client = new Client();
        }
        if (!client.canSend()) {
            try {
                client.connect(HOST,PORT,PASSWORD,TIMEOUT);
            } catch (InboundConnectionFailure e) {
                logger.error("连接失败",e);
            }
        }
    }

    /**
    * Description: 异步执行api命令
    * date: 2024/3/8 10:21

    * @author: Cai.Hongchao
    * @since JDK 8
    */
    public String sendAsyncApiCommand(String command, String args){
        connect();
        try {
            String result = null;
            if(client != null){
                result = client.sendAsyncApiCommand(command,args);
            }
            logger.info("命令执行结果:{}",result);
            return result;
        } catch (Exception e) {
            logger.error("命令执行失败",e);
            return "命令执行失败";
        }
    }

    public EslMessage sendSyncApiCommand(String command, String args){
        connect();
        EslMessage result = null;
        if(client != null){
            result = client.sendSyncApiCommand(command,args);
        }
        logger.info("命令执行结果:{}",result);
        return result;
    }

}
