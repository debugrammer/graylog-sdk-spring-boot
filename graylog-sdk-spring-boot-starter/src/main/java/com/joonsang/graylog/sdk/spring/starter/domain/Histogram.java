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

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class HistogramData implements Serializable {

        @Builder
        public HistogramData(String baseLabel, List<Statistics> statisticsList, List<StackedColumn> stackedColumns) {
            this.baseLabel = baseLabel;
            this.statisticsList = statisticsList;
            this.stackedColumns = stackedColumns;
        }

        @JsonProperty("base_label")
        private String baseLabel;

        @JsonProperty("statistics_list")
        private List<Statistics> statisticsList;

        @JsonProperty("stacked_columns")
        private List<StackedColumn> stackedColumns;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class StackedColumn implements Serializable {

        @Builder
        public StackedColumn(List<String> columnLabels, List<Statistics> statisticsList) {
            this.columnLabels = columnLabels;
            this.statisticsList = statisticsList;
        }

        @JsonProperty("column_labels")
        private List<String> columnLabels;

        @JsonProperty("statistics_list")
        private List<Statistics> statisticsList;
    }
}
