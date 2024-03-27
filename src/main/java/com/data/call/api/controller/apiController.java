package com.data.call.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class apiController {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 获取当前项目中的全部接口
     * */
    @RequestMapping(value = "getAllUrl.action")
    @ResponseBody
    public String getAllUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append("URL").append("--").append("Class").append("--").append("Function").append('\n');

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        int i=1;
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            sb.append(i+":").append(info.getPatternsCondition()).append("--");
            sb.append(method.getMethod().getDeclaringClass()).append("--");
            sb.append(method.getMethod().getName()).append('\n');
            i++;
        }

        System.out.println(sb);

        return sb.toString();

    }
}
