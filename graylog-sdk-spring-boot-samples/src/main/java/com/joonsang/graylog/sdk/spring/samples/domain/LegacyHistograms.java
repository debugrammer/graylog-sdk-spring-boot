package com.joonsang.graylog.sdk.spring.samples.domain;

import com.joonsang.graylog.sdk.spring.starter.domain.legacy.Histogram;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LegacyHistograms {

    @Builder
    public LegacyHistograms(List<String> labels, List<Histogram> histograms) {
        this.labels = labels;
        this.histograms = histograms;
    }

    private List<String> labels;

    private List<Histogram> histograms;
}
