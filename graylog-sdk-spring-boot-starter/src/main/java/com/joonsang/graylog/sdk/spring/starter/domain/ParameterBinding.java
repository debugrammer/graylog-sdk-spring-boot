package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParameterBinding {

    private final String type;

    private final String value;
}
