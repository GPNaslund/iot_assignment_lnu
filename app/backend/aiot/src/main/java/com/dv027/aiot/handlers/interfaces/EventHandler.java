package com.dv027.aiot.handlers.interfaces;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface EventHandler {
  Mono<ServerResponse> streamEvents(ServerRequest request);
}
