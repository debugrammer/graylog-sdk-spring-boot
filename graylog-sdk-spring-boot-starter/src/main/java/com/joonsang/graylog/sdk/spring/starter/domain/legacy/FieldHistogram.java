package com.joonsang.graylog.sdk.spring.starter.domain.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Legacy Field Histogram Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class FieldHistogram implements Serializable {

    @JsonProperty("field_histogram")
    private List<FieldHistogramData> fieldHistogram;
}
