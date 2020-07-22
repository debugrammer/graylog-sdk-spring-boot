package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joonsang.graylog.sdk.spring.starter.constant.TimeRangeType;
import lombok.Builder;
import lombok.Getter;

/**
 * Time Range
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class Timerange {

    private final TimeRangeType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer range;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String from;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String to;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String keyword;
}
