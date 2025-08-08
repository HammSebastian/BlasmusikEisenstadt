package com.hammsebastian.backend_stadtkapelle_eisenstadt.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Backend Stadtkapelle Eisenstadt",
                version = "1.0.0",
                description = "API for managing members, events, rehearsals and gigs"
        ),
        servers = {
                @Server(url = "/api/v1", description = "Base API Server")
        }
)
public class OpenApiConfiguration {
}