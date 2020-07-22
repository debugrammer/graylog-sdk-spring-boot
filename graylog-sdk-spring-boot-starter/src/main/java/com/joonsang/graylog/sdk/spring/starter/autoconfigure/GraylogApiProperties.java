package com.joonsang.graylog.sdk.spring.starter.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Graylog API Properties
 * @author debugrammer
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("graylog.sdk.api")
public class GraylogApiProperties {

    private String scheme = "http";

    private String host = "127.0.0.1";

    private Integer port = 9000;

    private String credentials = "";

    private Long timeout = 60000L;
}
