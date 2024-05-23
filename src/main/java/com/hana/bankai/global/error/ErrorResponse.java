package com.hana.bankai.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ErrorResponse.builder()
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}
