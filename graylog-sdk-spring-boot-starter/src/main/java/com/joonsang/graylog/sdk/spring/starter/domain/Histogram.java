package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Histogram
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Histogram implements Serializable {

    @Builder
    public Histogram(List<HistogramData> histogram) {
        this.histogram = histogram;
    }

    private List<HistogramData> histogram;

    @Builder
    @ToString
    public static class HistogramData implements Serializable {

        private final String label;

        @JsonProperty("statistics_list")
        private final List<Statistics> statisticsList;

        @JsonProperty("additional_columns")
        private final List<Value> additionalColumns;
    }
}
