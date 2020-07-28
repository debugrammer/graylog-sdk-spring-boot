package com.joonsang.graylog.sdk.spring.starter.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * Graylog Interval Timeunit
 * @author debugrammer
 * @since 2.0.0
 */
public class IntervalTimeunit {

    public enum Unit { seconds, minutes, hours, days, weeks, months }

    public static String get(Unit unit, Integer value) {
        return value.toString() + convertUnitToSymbol(unit);
    }

    private static String convertUnitToSymbol(Unit unit) {
        switch (unit) {
            case seconds:
                return "s";
            case minutes:
                return "m";
            case hours:
                return "h";
            case days:
                return "d";
            case weeks:
                return "w";
            case months:
                return "M";
        }

        return StringUtils.EMPTY;
    }
}
