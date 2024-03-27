package com.data.call.interceptor.mvc;

import com.data.call.interceptor.dto.ApiRequestDto;
import com.data.call.interceptor.service.ApiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author Cai.Hongchao
 */
@Component
public class ApiRequestInterceptor implements HandlerInterceptor {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Resource
    private ApiRequestService apiRequestService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String path = request.getRequestURI();
            String method = request.getMethod();
            // 获取输入流
            BufferedReader reader = request.getReader();
            StringBuilder requestBody = new StringBuilder();

            // 读取并拼接JSON字符串
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            logger.info("requestBody:{}", requestBody);
            LocalDateTime now = LocalDateTime.now();
            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 格式化当前时间
            String requestTime = now.format(formatter);

            ApiRequestDto apiRequestDto = new ApiRequestDto();
            apiRequestDto.setPath(path);
            apiRequestDto.setMethod(method);
            apiRequestDto.setParams(String.valueOf(requestBody));
            apiRequestDto.setRequestTime(requestTime);

            logger.info("请求路径：{}，请求方法：{}，请求参数：{}，请求时间：{}", path, method, requestBody, requestTime);
            apiRequestService.saveApiRequestLog(apiRequestDto);
            return true;
        } catch (IOException e) {
            logger.error("获取请求参数失败", e);
            return false;
        }
    }
}
