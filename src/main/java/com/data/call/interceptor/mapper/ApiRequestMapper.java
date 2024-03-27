package com.data.call.interceptor.mapper;

import com.data.call.interceptor.dto.ApiRequestDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chc
 */
@Mapper
public interface ApiRequestMapper {
    int insertApiRequestLog(ApiRequestDto apiRequestDto);
}