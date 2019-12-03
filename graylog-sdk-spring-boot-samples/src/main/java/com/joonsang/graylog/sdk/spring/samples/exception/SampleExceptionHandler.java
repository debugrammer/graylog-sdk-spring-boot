package com.joonsang.graylog.sdk.spring.samples.exception;

import com.joonsang.graylog.sdk.spring.starter.exception.GraylogServerException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SampleExceptionHandler {

    /**
     * Graylog Server Exception Handler
     */
    @ExceptionHandler(GraylogServerException.class)
    @ResponseBody
    public ResponseEntity<?> handleGraylogServerException(GraylogServerException e) {
        e.printStackTrace();

        return new ResponseEntity<>(
            Map.of("error_message", "Graylog server communication error."),
            HttpStatus.BAD_GATEWAY
        );
    }
}
