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
                @Server(url = "https://43.202.209.89.nip.io", description = "Prod Development"),
        }
)
@Configuration
public class SwaggerConfig {

    /**
     * Member 관련 API
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("Member")
                .displayName("Member API")
                .packagesToScan("com.cotato.cokerthon.domain.member.controller")
                .pathsToMatch("/api/members/**")
                .build();
    }

    /**
     * Auth 관련 API
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Auth")
                .displayName("Auth API")
                .packagesToScan("com.cotato.cokerthon.domain.auth.controller")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    /**
     * Group 관련 API
     */
    @Bean
    public GroupedOpenApi groupApi() {
        return GroupedOpenApi.builder()
                .group("Group")
                .displayName("Group API")
                .packagesToScan("com.cotato.cokerthon.domain.group.controller")
                .pathsToMatch("/api/groups/**")
                .build();
    }

    /**
     * 그룹별 Chore 관련 API
     */
    @Bean
    public GroupedOpenApi choreApi() {
        return GroupedOpenApi.builder()
                .group("Chore")
                .displayName("Chore API")
                .packagesToScan("com.cotato.cokerthon.domain.chore.controller")
                .pathsToMatch("/api/groups/{groupId}/chores/**")
                .build();
    }


}