package com.api.sysagua.docs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SysAgua API",
                description = "API para gerenciamento de distribuidora de água",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "Bearer"),
        servers = {
                @Server(url = "/", description = "Servidor padrão")
        }
)
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("Usuários")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("Produtos")
                .pathsToMatch("/products/**")
                .build();
    }

    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("Clientes")
                .pathsToMatch("/customers/**")
                .build();
    }

    @Bean
    public GroupedOpenApi stockApi() {
        return GroupedOpenApi.builder()
                .group("Estoque")
                .pathsToMatch("/stock/**")
                .build();
    }

    @Bean
    public GroupedOpenApi purchaseApi() {
        return GroupedOpenApi.builder()
                .group("Compras")
                .pathsToMatch("/purchases/**")
                .build();
    }

    @Bean
    public GroupedOpenApi supplierApi() {
        return GroupedOpenApi.builder()
                .group("Fornecedores")
                .pathsToMatch("/suppliers/**")
                .build();
    }
    @Bean
    public GroupedOpenApi deliveryPersonApi() {
        return GroupedOpenApi.builder()
                .group("Entregadores")
                .pathsToMatch("/deliveryPersons/**")
                .build();
    }

    @Bean
    public GroupedOpenApi transactionsApi() {
        return GroupedOpenApi.builder()
                .group("Transações")
                .pathsToMatch("/transactions/**")
                .build();
    }

}
