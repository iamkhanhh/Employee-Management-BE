package com.tlu.EmployeeManagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String cookieSchemeName = "cookieAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Employee Management System API")
                .version("1.0.0")
                .description("REST API documentation for Employee Management System. Supports both Cookie-based and Bearer token authentication.")
                .contact(new Contact()
                    .name("TLU Development Team")
                    .email("support@tlu.edu.vn"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
            .addSecurityItem(new SecurityRequirement()
                .addList(securitySchemeName)
                .addList(cookieSchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Enter your JWT token"))
                .addSecuritySchemes(cookieSchemeName, new SecurityScheme()
                    .name(cookieSchemeName)
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.COOKIE)
                    .name("access_token")
                    .description("JWT token stored in HttpOnly cookie")));
    }
}
