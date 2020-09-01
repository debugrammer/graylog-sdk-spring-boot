package com.joonsang.graylog.sdk.spring.starter;

import com.jayway.jsonpath.JsonPath;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeUnit;

import java.math.BigDecimal;
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

    /**
     * Get Double value from JSON path.
     * @param body JSON body
     * @param path JSON path to get
     * @return Converted Double value
     * @since 1.0.0
     */
    public static Double getDoubleFromJsonPath(String body, String path) {
        try {
            return JsonPath.parse(body).read(path, Double.class);
        } catch (NumberFormatException nfe) {
            return Double.NaN;
        }
    }

    /**
     * Parse Big Decimal number to Double.
     * @param number Big Decimal number
     * @return Parsed Double value
     * @since 2.0.0
     */
    public static Double parseBigDecimalToDouble(BigDecimal number) {
        if (number == null) {
            return null;
        }

        return number.doubleValue();
    }

    /**
     * Convert timestamp to formatted String date.
     * @param timezone timezone
     * @param timestamp timestamp to convert
     * @param timeUnit time unit to format
     * @return Converted String date
     * @since 1.0.0
     */
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
