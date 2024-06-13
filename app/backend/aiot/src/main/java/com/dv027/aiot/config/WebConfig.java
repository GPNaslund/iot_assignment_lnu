package com.dv027.aiot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Web configuration for the server.
 */
@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {

  /**
   * Sets up the CORS mappings for the server.
   * 
   * @param registry - The CORS registry that the server will use.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins("http://localhost:4200")
        .allowedMethods("GET", "POST")
        .allowedHeaders("*");
  }
}
