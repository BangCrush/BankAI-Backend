package com.hana.bankai.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.bankai.global.error.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.hana.bankai.global.error.ErrorCode.USER_ACCESS_FAIL;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("토큰 유휴성 검사에 실패했습니다.");
        log.error("Responding with access denied error. Message - {}", accessDeniedException.getMessage());

        // ErrorResponse 객체 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpServletResponse.SC_ACCEPTED) // 403 Forbidden 상태 코드 설정
                .code(USER_ACCESS_FAIL.getCode())
                .message(USER_ACCESS_FAIL.getMessage())
                .build();

        // JSON 응답을 작성하기 위해 content type을 설정합니다.
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden 상태 코드 설정

        // ErrorResponse 객체를 JSON 문자열로 변환
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // JSON 응답을 클라이언트에 전송
        response.getWriter().write(jsonResponse);
    }

}