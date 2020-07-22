package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Builder.Default
    private final String type = "auto";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer scaling;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String unit;
}
