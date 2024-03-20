package com.data.call.record.dto;

import lombok.Data;

/**
 *@author:Cai.Hongchao
 *@create: 2024-03-12 16:21
 *@Description: 通话记录实体类
 */

@Data
public class CallRecordDto {
    private Long sid;
    private String caller;
    private String callee;
    private String direction;
    private String createTime;
    private String answerTime;
    private String bridgeTime;
    private String endTime;
    private String duration;
    private String uuid;
    private String callType;
    private String BLegUuid;
}
