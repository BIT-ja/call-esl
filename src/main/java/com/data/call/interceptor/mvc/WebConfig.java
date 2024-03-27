package com.data.call.interceptor.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Cai.Hongchao
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private ApiRequestInterceptor apiRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiRequestInterceptor).addPathPatterns("/**");
    }
}
