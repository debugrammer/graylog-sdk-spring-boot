package com.joonsang.graylog.sdk.spring.starter.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Statistics
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Statistics implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String field;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double average;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer cardinality;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer count;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double max;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double min;

    @JsonProperty("std_deviation")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double stdDeviation;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sum;

    @JsonProperty("sum_of_squares")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sumOfSquares;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double variance;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Double> percentiles;

    @JsonProperty("percentile_ranks")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> percentileRanks;
}
