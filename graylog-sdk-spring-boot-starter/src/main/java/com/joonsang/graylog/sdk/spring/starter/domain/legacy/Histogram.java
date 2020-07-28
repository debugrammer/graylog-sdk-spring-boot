package com.joonsang.graylog.sdk.spring.starter.domain.legacy;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Legacy Histogram Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Histogram implements Serializable {

    private List<HistogramData> histogram;
}
