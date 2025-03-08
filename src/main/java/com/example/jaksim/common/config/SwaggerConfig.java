package com.example.jaksim.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "Bearer Authentication";

        Info info = new Info()
                .title("Jaksim API Documentation")
                .version("v1.0.0")
                .description("Jaksim 프로젝트의 API 문서입니다.")
                .contact(new Contact()
                        .name("Jaksim Team")
                        .email("periq23@gmail.com")
                        .url("https://jaksim.site"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

        Server devServer = new Server()
                .url("http://ec2-43-201-22-201.ap-northeast-2.compute.amazonaws.com/")
                .description("Dev Server");

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Server");

        Server prodServer = new Server()
                .url("http://jaksim.site/")
                .description("Production Server");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName);

        return new OpenAPI()
                .info(info)
                .servers(Arrays.asList(devServer, localServer, prodServer))
                .addSecurityItem(securityRequirement);
    }
}