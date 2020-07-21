package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SortConfig {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String direction;

    private final String field;

    private final String order;
}
