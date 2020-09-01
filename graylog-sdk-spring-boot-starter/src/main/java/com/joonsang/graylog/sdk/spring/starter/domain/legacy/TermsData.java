package com.joonsang.graylog.sdk.spring.starter.domain.legacy;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Legacy Terms Data Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class TermsData implements Serializable {

    private List<String> labels;

    private Integer data;

    private Double ratio;
}
