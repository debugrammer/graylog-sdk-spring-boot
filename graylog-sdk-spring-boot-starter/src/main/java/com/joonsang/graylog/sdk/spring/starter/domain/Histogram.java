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

        @JsonProperty("base_label")
        private final String baseLabel;

        @JsonProperty("statistics_list")
        private final List<Statistics> statisticsList;

        @JsonProperty("stacked_columns")
        private final List<StackedColumn> stackedColumns;
    }

    @Builder
    @ToString
    public static class StackedColumn implements Serializable {

        @JsonProperty("column_labels")
        private final List<String> columnLabels;

        @JsonProperty("statistics_list")
        private final List<Statistics> statisticsList;
    }
}
