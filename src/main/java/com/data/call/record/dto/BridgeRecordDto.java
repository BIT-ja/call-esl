package com.data.call.record.dto;

import lombok.Data;

/**
 * 桥接记录
 *
 * @author:Cai.Hongchao
 * @create: 2024-03-21 10:15:00
 */

@Data
public class BridgeRecordDto {
    private String sid;
    private String bridgeTime;
    private String uuidA;
    private String uuidB;
}
