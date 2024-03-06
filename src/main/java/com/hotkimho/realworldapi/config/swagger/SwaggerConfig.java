package com.hotkimho.realworldapi.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {


        return new OpenAPI().info(new Info()
                .title("Real World API")
                .description("Real World API")
                .version("v1")
                .termsOfService("http://swagger.io/terms/")
                .license(new io.swagger.v3.oas.models.info.License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
