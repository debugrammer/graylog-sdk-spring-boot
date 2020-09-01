package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Terms
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Terms implements Serializable {

    @Builder
    public Terms(List<TermsData> terms) {
        this.terms = terms;
    }

    private List<TermsData> terms;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class TermsData implements Serializable {

        @Builder
        public TermsData(List<String> baseLabels, List<Statistics> statisticsList, List<StackedColumn> stackedColumns) {
            this.baseLabels = baseLabels;
            this.statisticsList = statisticsList;
            this.stackedColumns = stackedColumns;
        }

        @JsonProperty("base_labels")
        private List<String> baseLabels;

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
