package com.dv027.aiot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dv027.aiot.handlers.interfaces.FeedHandler;
import com.dv027.aiot.handlers.dto.LedStatus;
import com.dv027.aiot.services.interfaces.LedService;
import com.dv027.aiot.services.pojo.LedStatePojo;

import reactor.core.publisher.Mono;

/**
 * Handler class with methods for publishing LedState and getting latest
 * LedState.
 */
@Component
public class LedDataHandler implements FeedHandler {
  private final LedService service;

  /**
   * Creates a new instance of LedControlHandler.
   * 
   * @param service - Class that implements the LedService interface.
   */
  @Autowired
  public LedDataHandler(LedService service) {
    this.service = service;
  }

  /**
   * Handler method used to publish a LedStatus value to a LED mqtt feed.
   * 
   * @param request - The incoming server request.
   * @return Mono<ServerResponse> - The outgoing server response.
   */
  public Mono<ServerResponse> publishToFeed(ServerRequest request) {
    Mono<LedStatus> publishValue = request.bodyToMono(LedStatus.class);
    return publishValue.flatMap(value -> {
      try {
        service.publishLedState(value.getState());
        return ServerResponse.ok().build();
      } catch (IllegalArgumentException e) {
        return ServerResponse.badRequest().bodyValue("Invalid request body");
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return ServerResponse.status(500).bodyValue("Something went wrong, try again later.");
      }
    })
        .onErrorResume(e -> {
          return ServerResponse.badRequest().bodyValue("Invalid request body");
        });
  }

  /**
   * Handler method for getting the latest stored LedState.
   * 
   * @param request - The incoming server request.
   * @return Mono<ServerResponse> - The outgoing server response.
   */
  public Mono<ServerResponse> getFeedStatus(ServerRequest request) {
    try {
      Mono<LedStatePojo> state = this.service.getLedState();
      return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(state, LedStatePojo.class);
    } catch (Exception e) {
      return ServerResponse.status(500).bodyValue("Something went wrong");
    }
  }
}
