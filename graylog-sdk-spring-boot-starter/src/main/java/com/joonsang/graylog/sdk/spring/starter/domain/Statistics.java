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

    @JsonProperty("field")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String field;

    @JsonProperty("average")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double average;

    @JsonProperty("cardinality")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer cardinality;

    @JsonProperty("count")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer count;

    @JsonProperty("max")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double max;

    @JsonProperty("min")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double min;

    @JsonProperty("std_deviation")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double stdDeviation;

    @JsonProperty("sum")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sum;

    @JsonProperty("sum_of_squares")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double sumOfSquares;

    @JsonProperty("variance")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Double variance;

    @JsonProperty("percentiles")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Double> percentiles;

    @JsonProperty("percentile_ranks")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> percentileRanks;
}
