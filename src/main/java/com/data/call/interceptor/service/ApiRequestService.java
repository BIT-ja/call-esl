package com.data.call.interceptor.service;

import com.data.call.interceptor.dto.ApiRequestDto;
import com.data.call.interceptor.mapper.ApiRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:Cai.Hongchao
 * @description: 保存api请求日志
 */
@Service
public class ApiRequestService {
    private final Logger logger = LoggerFactory.getLogger(ApiRequestService.class);
    @Resource
    private ApiRequestMapper apiRequestMapper;
    public void saveApiRequestLog(ApiRequestDto apiRequestDto) {
        new Thread(() -> {
            try {
                int insertRes = apiRequestMapper.insertApiRequestLog(apiRequestDto);
                if (insertRes > 0) {
                    logger.info("保存api请求日志成功");
                } else {
                    logger.error("保存api请求日志失败");
                }
            } catch (Exception e) {
                logger.error("保存api请求日志失败", e);
            }
        }).start();
    }
}
