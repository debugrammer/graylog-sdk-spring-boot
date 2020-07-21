package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Parameter {

    private final String type;

    private final String name;

    private final String title;

    private final String description;

    @JsonProperty("data_type")
    private final String dataType;

    @JsonProperty("default_value")
    private final String defaultValue;

    private final Boolean optional;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ParameterBinding binding;
}
