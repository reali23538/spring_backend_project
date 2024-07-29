package com.seed.sbp.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "할일 관리 플랫폼", description = "할일 관리 플랫폼의 API 입니다.", version = "1.0.0"))
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi todoAPI() {
        String[] paths = {"/todos/**"};

        return GroupedOpenApi.builder()
                .group("todo")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi authAPI() {
        String[] paths = {"/login", "/refresh"};

        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch(paths)
                .build();
    }

}
