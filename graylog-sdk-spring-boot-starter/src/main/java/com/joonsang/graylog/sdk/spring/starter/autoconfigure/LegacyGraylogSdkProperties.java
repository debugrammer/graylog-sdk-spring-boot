package com.joonsang.graylog.sdk.spring.starter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Legacy Graylog SDK Properties
 * (Graylog version < 3.2)
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("graylog.sdk.legacy")
public class LegacyGraylogSdkProperties {

    private String timezone = "US/Eastern";
}
