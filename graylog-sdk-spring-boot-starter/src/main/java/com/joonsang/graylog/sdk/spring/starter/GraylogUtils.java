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
     * Convert maybe number value to Double.
     * @param value maybe number value
     * @return Converted Double value
     * @since 2.0.1
     */
    public static Double valueToDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            BigDecimal bigDecimalVal = (BigDecimal) value;

            return bigDecimalVal.doubleValue();
        }

        if (value instanceof Long) {
            Long longVal = (Long) value;

            return longVal.doubleValue();
        }

        if (value instanceof Float) {
            Float floatVal = (Float) value;

            return floatVal.doubleValue();
        }

        if (value instanceof String) {
            String stringVal = (String) value;

            try {
                return Double.valueOf(stringVal);
            } catch (NumberFormatException nfe) {
                return Double.NaN;
            }
        }

        try {
            return (Double) value;
        } catch (ClassCastException cce) {
            return Double.NaN;
        }
    }

    /**
     * Convert maybe number value to Integer.
     * @param value maybe number value
     * @return Converted Integer value
     * @since 2.0.1
     */
    public static Integer valueToInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            BigDecimal bigDecimalVal = (BigDecimal) value;

            return bigDecimalVal.intValue();
        }

        if (value instanceof Long) {
            Long longVal = (Long) value;

            return longVal.intValue();
        }

        if (value instanceof Double) {
            Double doubleVal = (Double) value;

            return doubleVal.intValue();
        }

        if (value instanceof Float) {
            Float floatVal = (Float) value;

            return floatVal.intValue();
        }

        if (value instanceof String) {
            String stringVal = (String) value;

            try {
                return Integer.valueOf(stringVal);
            } catch (NumberFormatException nfe) {
                return 0;
            }
        }

        try {
            return (Integer) value;
        } catch (ClassCastException cce) {
            return 0;
        }
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
