package com.joonsang.graylog.sdk.spring.starter.domain.legacy;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Legacy Terms Domain
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Terms implements Serializable {

    private List<TermsData> terms;
}
