package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joonsang.graylog.sdk.spring.starter.constant.IntervalType;
import lombok.Builder;
import lombok.Getter;

/**
 * Interval
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class Interval {

    private final IntervalType type;

    /**
     * Work with IntervalType.auto type of {@link Interval}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer scaling;

    /**
     * Work with IntervalType.timeunit type of {@link Interval}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer value;

    /**
     * Work with IntervalType.timeunit type of {@link Interval}
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String unit;
}
