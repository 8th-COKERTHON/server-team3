package com.cotato.cokerthon.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Cokerthon API Docs",
                version = "v1.0.0",
                description = "Cokerthon Backend API Documentation",
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development"),
                @Server(url = "https://15.164.164.141.nip.io", description = "Prod Development"),
        }
)
@Configuration
public class SwaggerConfig {

    /**
     *  관련 API
     */
    @Bean
    public GroupedOpenApi rouletteApi() {
        return GroupedOpenApi.builder()
                .group("Roulette")
                .displayName("roulette API")
                .packagesToScan("com.cotato.cokerthon.domain.roulette.controller")
                .pathsToMatch("/api/roulette/**")
                .build();
    }


}