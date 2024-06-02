package com.hana.bankai.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    // Bearer
    // JWT 혹은 OAuth에 대한 토큰을 사용한다. (RFC 6750)
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Start JwtAuthenticationFilter: Request Header 에서 JWT 토큰을 추출합니다.");
        // 1. Request Header 에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);

        // 2. validateToken 으로 토큰 유효성 검사
        log.info("Start JwtAuthenticationFilter: validateToken 으로 토큰 유효성 검사를 실시합니다.");
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Redis 에 해당 accessToken logout 여부 확인
            String isLogout = (String) redisTemplate.opsForValue().get(token);
            log.info("Start isLogout: logout 여부를 확인합니다.");
            log.info("isLogout = {}", isLogout);
            log.info("ObjectUtils.isEmpty(isLogout) = {}", ObjectUtils.isEmpty(isLogout));


            if (ObjectUtils.isEmpty(isLogout)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                log.info("Start JwtAuthenticationFilter: 토큰이 유효하여 토큰에서 Authentication 객체를 불러와 SecurityContext 에 저장합니다.");
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                log.info("authentication.getName() = {}", authentication.getName()); // getName은 사용자의 ID를 의미
                log.info("authentication.getAuthorities() = {}", authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 토근이 없을 때 실행
        // 접근 권한 없으면 AccessDeniedException 발생
        chain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
