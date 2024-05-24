package com.hana.bankai.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final int status;
    private final String message;
    @JsonInclude(NON_NULL)
    private T data;

    private ApiResponse() {
        throw new IllegalStateException();
    }

    public static <T> ApiResponse<T> success(SuccessCode success) {
        return new ApiResponse<>(success.getHttpStatusValue(), success.getMessage());
    }

    public static <T> ApiResponse<T> success(SuccessCode success, T data){
        return new ApiResponse<>(success.getHttpStatusValue(), success.getMessage(), data);
    }
}
