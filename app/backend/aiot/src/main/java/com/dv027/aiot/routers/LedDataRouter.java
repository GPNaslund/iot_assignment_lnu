package com.dv027.aiot.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dv027.aiot.handlers.interfaces.FeedHandler;

/**
 * Router class for routing requests to handler methods.
 */
@Configuration
public class LedDataRouter {

  /**
   * Method for routing the specified routes.
   * 
   * @param handler - The handler that acts on the incoming requests.
   * @return RouterFunction<ServerResponse> - The resulting response.
   */
  @Bean("LedActionRoute")
  public RouterFunction<ServerResponse> route(FeedHandler handler) {

    return RouterFunctions
        .route()
        .POST("api/v1/led-control", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::publishToFeed)
        .GET("api/v1/led-control/status", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getFeedStatus)
        .build();
  }
}
