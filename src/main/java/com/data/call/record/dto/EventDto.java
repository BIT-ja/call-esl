package com.data.call.record.dto;

import lombok.Data;

/**
 *@author:Cai.Hongchao
 *@create: 2024-03-01 14:53
 *@Description: 通话数据存储
 */
@Data
public class EventDto {
    private String id;
    private String name;
    private String phone;
    private String time;
    private String duration;
    private String type;
    private String status;
    private String remark;
    private String date;
}
