package com.joonsang.graylog.sdk.spring.samples.domain;

import com.joonsang.graylog.sdk.spring.starter.domain.legacy.Statistics;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoStatistics {

    @Builder
    public TwoStatistics(Statistics first, Statistics second) {
        this.first = first;
        this.second = second;
    }

    private Statistics first;

    private Statistics second;
}
