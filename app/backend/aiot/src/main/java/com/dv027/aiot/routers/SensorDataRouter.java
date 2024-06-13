package com.dv027.aiot.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dv027.aiot.handlers.interfaces.DataHandler;

import org.springframework.http.MediaType;

/**
 * Router class for SensorData routes.
 */
@Configuration
public class SensorDataRouter {

  /**
   * Method for handling incoming requests to specified routes.
   * 
   * @param handler - Class for handling incoming requests.
   * @return RouterFunction<ServerResponse> - The resulting response.
   */
  @Bean("SensorDataRoute")
  public RouterFunction<ServerResponse> route(DataHandler handler) {
    return RouterFunctions
        .route()
        .GET("/api/v1/sensor-data/shortterm", RequestPredicates.accept(MediaType.APPLICATION_JSON),
            handler::getShortTermData)
        .GET("/api/v1/sensor-data/longterm", RequestPredicates.accept(MediaType.APPLICATION_JSON),
            handler::getLongTermData)
        .build();
  }
}
