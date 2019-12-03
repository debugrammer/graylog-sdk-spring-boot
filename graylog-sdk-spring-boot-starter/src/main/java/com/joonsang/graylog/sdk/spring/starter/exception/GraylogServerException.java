package com.joonsang.graylog.sdk.spring.starter.exception;

import java.io.IOException;

/**
 * GraylogServerException will be produced when
 * Graylog SDK fails communicating with Graylog server.
 *
 * @author debugrammer
 * @since 1.1.0
 */
public class GraylogServerException extends IOException {

    public GraylogServerException(String message) {
        super(message);
    }
}
