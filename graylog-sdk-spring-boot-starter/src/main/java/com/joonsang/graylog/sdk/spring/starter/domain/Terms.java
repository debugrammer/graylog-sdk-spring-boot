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

    @Builder
    @ToString
    public static class TermsData implements Serializable {

        @JsonProperty("base_labels")
        private final List<String> baseLabels;

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
