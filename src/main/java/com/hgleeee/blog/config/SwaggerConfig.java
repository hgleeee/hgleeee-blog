package com.hgleeee.blog.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "hgleeee 블로그 API 문서",
                description = "hgleeee 블로그 API spec을 정리한 문서입니다.",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicAPI() {
        return GroupedOpenApi.builder()
                .group("spring-test")
                .pathsToMatch("/test/**")
                .packagesToScan("com.hgleeee.blog.controller")
                .build();
    }

}
