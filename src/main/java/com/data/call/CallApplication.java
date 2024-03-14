package com.data.call;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description:
 * @Version: 1.0
 * @author:Cai.Hongchao
 * @create: 2023-11-26 16:36
 */
@SpringBootApplication
@MapperScan("com.data.call.record.mapper")
@EnableAsync
public class CallApplication {

    public static void main(String[] args) {
        SpringApplication.run(CallApplication.class, args);
    }


}
