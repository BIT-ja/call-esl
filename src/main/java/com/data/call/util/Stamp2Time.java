package com.data.call.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Stamp2Time {
    /**
     * 将时间戳转换为指定格式的日期时间字符串
     * @param stamp 时间戳
     * @return formattedDateTime 日期时间字符串
     */
    public static String stamp2Time(String stamp) {
        long timestamp = Long.parseLong(stamp)/1000;
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 使用指定时区
        ZoneId zoneId = ZoneId.of("Asia/Shanghai"); // 可以根据需要修改时区

        // 使用DateTimeFormatter将Instant转换为指定格式的日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = formatter.format(instant.atZone(zoneId));
        return formattedDateTime;
    }
}
