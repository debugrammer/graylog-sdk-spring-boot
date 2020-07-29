package com.joonsang.graylog.sdk.spring.samples.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joonsang.graylog.sdk.spring.starter.domain.legacy.FieldHistogram;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LegacyFieldHistograms {

    @Builder
    public LegacyFieldHistograms(List<String> labels, List<FieldHistogram> fieldHistograms) {
        this.labels = labels;
        this.fieldHistograms = fieldHistograms;
    }

    private List<String> labels;

    @JsonProperty("field_histograms")
    private List<FieldHistogram> fieldHistograms;
}
