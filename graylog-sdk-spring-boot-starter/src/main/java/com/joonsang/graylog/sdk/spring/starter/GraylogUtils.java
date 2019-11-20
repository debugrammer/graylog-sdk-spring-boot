package com.joonsang.graylog.sdk.spring.starter;

import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeUnit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Graylog SDK Utils
 * @author debugrammer
 * @since 1.0.0
 */
public class GraylogUtils {

    public static Double getDoubleFromJsonPath(String body, String path) {
        try {
            return JsonPath.parse(body).read(path, Double.class);
        } catch (NumberFormatException nfe) {
            return Double.NaN;
        }
    }

    public static String convertTimestampToStringDate(String timezone, long timestamp, String timeUnit) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp),
            TimeZone.getTimeZone(timezone).toZoneId()
        );

        switch (timeUnit) {
            case TimeUnit.YEAR:
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy"));
            case TimeUnit.QUARTER:
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-QQQ"));
            case TimeUnit.MONTH:
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            case TimeUnit.DAY:
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
