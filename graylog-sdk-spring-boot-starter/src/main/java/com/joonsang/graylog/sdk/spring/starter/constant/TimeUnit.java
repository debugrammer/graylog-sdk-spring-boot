package com.joonsang.graylog.sdk.spring.starter.constant;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class TimeUnit {

    public static final String YEAR = "year";

    public static final String QUARTER = "quarter";

    public static final String MONTH = "month";

    public static final String WEEK = "week";

    public static final String DAY = "day";

    public static final String HOUR = "hour";

    public static final String MINUTE = "minute";

    public static List<String> getFields() {
        return ImmutableList.of(YEAR, QUARTER, MONTH, WEEK, DAY, HOUR, MINUTE);
    }

    public static boolean contains(String timeUnit) {
        return getFields().contains(timeUnit);
    }
}
