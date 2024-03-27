package com.data.call.interceptor.dto;

import lombok.Data;

@Data
public class ApiRequestDto {
    private Long id;

    private String path;

    private String method;

    private String params;

    private String requestTime;
}
