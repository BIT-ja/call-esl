package com.data.call;

import com.data.call.listener.ESLListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class CallApplication implements CommandLineRunner{

    @Resource
    private ESLListener eslListener;
    public static void main(String[] args) {
        SpringApplication.run(CallApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        eslListener.inboundFS();
    }
}
