package com.dv027.aiot.routers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dv027.aiot.handlers.interfaces.EventHandler;

/**
 * Class that handles the data events route.
 */
@Configuration
public class EventRouter {

  /**
   * Router method for routing the specified routes.
   * 
   * @param eventHandler - The handler used for acting on the requests.
   * @return RouterFunction<ServerResponse> - The resulting response.
   */
  @Bean("EventRoute")
  public RouterFunction<ServerResponse> route(EventHandler eventHandler) {
    return RouterFunctions
        .route()
        .GET("api/v1/data-events", RequestPredicates.accept(MediaType.TEXT_EVENT_STREAM), eventHandler::streamEvents)
        .build();
  }
}
