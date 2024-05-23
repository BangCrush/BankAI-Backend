package com.hana.bankai.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(
        title = "BankAI API 명세서",
        description = "BankAI 서비스 API 명세서",
        version = "1.0"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userOpenApi() {
        String[] paths = {"/api/user/**"}; // 해당 경로에 매칭되는 API를 그룹화하여 문서화

        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi accountOpenApi() {
        String[] paths = {"/api/account/**"};

        return GroupedOpenApi.builder()
                .group("account")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi productOpenApi() {
        String[] paths = {"/api/product/**"};

        return GroupedOpenApi.builder()
                .group("product")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi autoTransferOpenApi() {
        String[] paths = {"/api/auto_transfer/**"};

        return GroupedOpenApi.builder()
                .group("auto_transfer")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi accountHistoryOpenApi() {
        String[] paths = {"/api/account_history/**"};

        return GroupedOpenApi.builder()
                .group("account_history")
                .pathsToMatch(paths)
                .build();
    }

}
