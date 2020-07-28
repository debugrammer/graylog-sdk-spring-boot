package com.joonsang.graylog.sdk.spring.starter.domain.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Legacy Field Histogram Data Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FieldHistogramData implements Serializable {

    private String label;

    @JsonProperty("total_count")
    private Integer totalCount;

    private Integer count;

    private Double min;

    private Double max;

    private Double total;

    private Double mean;

    private Integer cardinality;
}
