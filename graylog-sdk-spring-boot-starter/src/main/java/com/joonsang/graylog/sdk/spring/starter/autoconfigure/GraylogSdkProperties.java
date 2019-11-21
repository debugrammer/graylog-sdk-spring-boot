package com.joonsang.graylog.sdk.spring.starter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Graylog SDK Properties
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("graylog.sdk")
public class GraylogSdkProperties {

    private String timezone = "US/Eastern";
}
