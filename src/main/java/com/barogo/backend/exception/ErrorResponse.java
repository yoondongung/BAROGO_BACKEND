package com.barogo.backend.exception;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;


@Value
public class ErrorResponse {
    int status;
    String message;

    @Builder
    public ErrorResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}
