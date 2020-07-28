package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

        private final List<String> labels;

        @JsonProperty("statistics_list")
        private final List<Statistics> statisticsList;

        @JsonProperty("additional_columns")
        private final List<Value> additionalColumns;
    }
}
