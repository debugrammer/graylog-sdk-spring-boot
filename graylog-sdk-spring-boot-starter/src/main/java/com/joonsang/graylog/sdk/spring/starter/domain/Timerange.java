package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Timerange {

    private final String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer range;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String from;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String to;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String keyword;
}
