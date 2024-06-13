package com.dv027.aiot.handlers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dv027.aiot.handlers.interfaces.EventHandler;
import com.dv027.aiot.services.interfaces.EventService;

import reactor.core.publisher.Mono;

/**
 * Handler class with method for returning an event stream.
 */
@Component
public class SensorDataEventHandler implements EventHandler {
  private final EventService service;

  /**
   * Creates an instance of SensorDataEventHandler.
   * 
   * @param service - An instance that implements the EventService interface.
   */
  public SensorDataEventHandler(EventService service) {
    this.service = service;
  }

  /**
   * Handler method for returning an event stream.
   * 
   * @param request - The incoming server request.
   * @return Mono<ServerResponse> - The outgoing server response.
   */
  @Override
  public Mono<ServerResponse> streamEvents(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(service.getEventStream(), String.class);
  }
}
