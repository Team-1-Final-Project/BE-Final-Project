package com.innovation.backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "EARTH,US API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(
            name = "이노베이션 캠프 1조",
            url = "https://scythe-utensil-88c.notion.site/1-65467334f25a462e82818df8f52b34fd"
        )
    )
)

@Configuration
public class OpenApiConfig {}
// Swagger 설정