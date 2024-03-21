package com.data.call.record.controller;

import com.data.call.cmd.CommandExecutorHandler;
import com.data.call.record.service.RecordService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@author:Cai.Hongchao
 *@create: 2024-03-01 14:56
 *@Description: esl数据操作
 */

@RestController
@RequestMapping("/esl")
public class ESLController {

    @Resource
    private RecordService recordService;

    @Resource
    private CommandExecutorHandler commandExecutorHandler;

    @RequestMapping("/show-reg-agent")
    public Map<String, Object> reg(@RequestBody Map<String, String> map) {
        Map<String, Object> resMap = recordService.getRegister(map);
        return resMap;
    }

    @RequestMapping("/async-api")
    public Map<String, Object> asyncApi(@RequestBody Map<String, String> map) {
        Map<String, Object> resMap = new HashMap<>();
        String res = commandExecutorHandler.sendAsyncApiCommand(map.get("command"), map.get("args"));
        resMap.put("data", res);
        resMap.put("code", 200);
        resMap.put("msg", "success");
        return resMap;
    }

    @RequestMapping("/sync-api")
    public Map<String, Object> syncApi(@RequestBody Map<String, String> map) {
        Map<String, Object> resMap = new HashMap<>();
        List<String> res = commandExecutorHandler.sendSyncApiCommand(map.get("command"), map.get("args")).getBodyLines();
        resMap.put("data", res);
        resMap.put("code", 200);
        resMap.put("msg", "success");
        return resMap;
    }
}
