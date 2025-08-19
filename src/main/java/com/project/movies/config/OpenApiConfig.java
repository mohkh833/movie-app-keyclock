package com.project.movies.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Alibou",
                        email = "contact@aliboucoding.com",
                        url = "https://aliboucoding.com/courses"
                ),
                description = "OpenAPI documentation for Spring Security",
                title = "OpenAPI Specification",
                version = "1.0",
                license = @License(
                        name = "License Name",  // Fixed typo
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server (
                        description = "Local ENV",
                        url = "http://localhost:8088/api/v1"  // Fixed missing colon
                ),
                @Server (
                        description = "Prod ENV",
                        url = "https://aliboucoding.com/courses"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {
}
