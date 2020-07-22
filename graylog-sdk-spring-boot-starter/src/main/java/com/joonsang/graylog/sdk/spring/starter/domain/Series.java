package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/**
 * Series
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class Series {

    private final String id;

    private final String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String field;
}
