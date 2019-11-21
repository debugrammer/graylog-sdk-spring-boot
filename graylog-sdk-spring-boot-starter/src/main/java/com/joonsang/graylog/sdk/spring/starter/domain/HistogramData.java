package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.*;

import java.io.Serializable;

/**
 * Histogram Data Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class HistogramData implements Serializable {

    private String label;

    private Integer data;
}
