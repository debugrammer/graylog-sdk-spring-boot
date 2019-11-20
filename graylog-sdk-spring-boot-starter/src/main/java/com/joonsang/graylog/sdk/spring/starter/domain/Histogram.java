package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Histogram Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Histogram implements Serializable {

    private List<HistogramData> histogram;
}
