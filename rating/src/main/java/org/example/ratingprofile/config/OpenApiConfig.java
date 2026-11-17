package org.example.ratingprofile.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ratingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rating service")
                        .description("Lagrer vurderinger av profiler og publiserer RatingCreated til Kafka")
                        .version("1.0"))
                .addServersItem(new Server().url("/").description("Via gateway"));
    }
}