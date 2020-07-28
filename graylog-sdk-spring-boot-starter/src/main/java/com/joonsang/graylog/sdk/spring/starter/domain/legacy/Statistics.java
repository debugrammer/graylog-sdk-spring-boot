package com.joonsang.graylog.sdk.spring.starter.domain.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Legacy Statistics Domain
 * (Graylog version < 3.2)
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Statistics implements Serializable {

    private Integer count;

    private Double sum;

    @JsonProperty("sum_of_squares")
    private Double sumOfSquares;

    private Double mean;

    private Double min;

    private Double max;

    private Double variance;

    @JsonProperty("std_deviation")
    private Double stdDeviation;

    private Integer cardinality;
}
