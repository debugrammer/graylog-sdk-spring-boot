package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.*;

import java.util.List;

/**
 * Value
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class Value {

    private List<String> labels;

    private Statistics statistics;
}
