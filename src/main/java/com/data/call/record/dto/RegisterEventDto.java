package com.data.call.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author:Cai.Hongchao
 * @create: 2024-03-05 15:16
 * @Description: 注册事件实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEventDto {
    private Long id;
    private int userNo;
    private String contact;
    private String startTime;
    private String endTime;
    private String duration;
    private int endType;
    private String callId;
}
