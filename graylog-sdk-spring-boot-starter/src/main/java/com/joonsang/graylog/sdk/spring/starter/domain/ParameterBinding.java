package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * Parameter Binding
 * @author debugrammer
 * @since 2.0.0
 */
@Builder
@Getter
public class ParameterBinding {

    private final String type;

    private final String value;
}
