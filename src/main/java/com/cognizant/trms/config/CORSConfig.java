package com.cognizant.trms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
    Author: Aravindan Dandapani
*/

@Configuration
public class CORSConfig {
        @Bean
        public WebMvcConfigurer corsConfigurer() {

            // TODO - https://www.programcreek.com/java-api-examples/index.php?api=org.springframework.web.cors.CorsConfigurationSource
            // TODO - Update the CORS headers to specific Domains.
            // TODO - Eliminate the duplicate in MultiHttpSecurityConfig.Java
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                    .allowedOrigins("*");
                }
            };
        }
}
