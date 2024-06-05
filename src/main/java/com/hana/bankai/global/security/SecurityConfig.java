package com.hana.bankai.global.security;

import com.hana.bankai.global.security.jwt.JwtAccessDeniedHandler;
import com.hana.bankai.global.security.jwt.JwtAuthenticationEntryPoint;
import com.hana.bankai.global.security.jwt.JwtAuthenticationFilter;
import com.hana.bankai.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 로그인 하지 않아도 접근 가능한 주소 설정
    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**", // Swagger UI에 대한 경로
            "/bankAi-docs/**",
            "/register/**",
            "/login/**",
            "/admin/**", // 관리자 로그인 구현 이후 삭제 예정
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) throws Exception {
        // CSRF, CORS 해제
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        // 세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        // FormLogin, BasicHttp 비활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        // 권한이 없거나 부족할 때 발생할 예외 처리
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 여부 검사
                .accessDeniedHandler(jwtAccessDeniedHandler) // 인가 여부 검사
        );

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll()
//                        .requestMatchers("/admin/**").hasAnyRole("ADMIN") // 해당 URI에 있는 경로는 관리자만 접근 가능
                        .requestMatchers(AUTH_WHITELIST).permitAll() // AUTH_WHITELIST에 있는 경로는 인증 없이 접근 허용
                        .anyRequest().hasAnyRole("USER", "ADMIN") // 나머지 모든 요청은 ROLE_USER 또는 ROLE_ADMIN 역할 필요
        );

        return http.build();
    }

}