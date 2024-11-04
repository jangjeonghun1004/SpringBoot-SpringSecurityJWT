package com.example.demo.config.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public SwaggerConfig() { }

    @Bean
    OpenAPI openAPI() {
        return (new OpenAPI()).components(new Components()).info(this.apiInfo());
    }

    private Info apiInfo() {
        return (new Info()).title("Springdoc 테스트").description("Springdoc을 사용한 Swagger UI 테스트").version("1.0.0");
    }
}
