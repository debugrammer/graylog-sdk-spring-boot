package com.joonsang.graylog.sdk.spring.starter.domain;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Terms
 * @author debugrammer
 * @since 2.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Terms implements Serializable {

    private List<TermsData> terms;

    @Builder
    static class TermsData implements Serializable {

        private final List<TermsLabel> labelMap;

        private final Statistics statistics;

        @Builder
        static class TermsLabel implements Serializable {

            private final String fieldName;

            private final String fieldValue;
        }
    }
}
