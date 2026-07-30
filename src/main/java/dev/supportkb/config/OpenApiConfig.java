package dev.supportkb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI supportKnowledgeBaseOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Support Knowledge Base API")
                .version("v1")
                .description("REST API for searchable technical support knowledge articles."));
  }
}
