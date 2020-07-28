package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Value
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Value implements Serializable {

    @Builder
    public Value(List<String> labels, Statistics statistics) {
        this.labels = labels;
        this.statistics = statistics;
    }

    private List<String> labels;

    private Statistics statistics;
}
