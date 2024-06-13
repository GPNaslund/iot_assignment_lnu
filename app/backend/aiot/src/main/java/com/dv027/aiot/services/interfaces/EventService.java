package com.dv027.aiot.services.interfaces;

import reactor.core.publisher.Flux;

public interface EventService {
  Flux<String> getEventStream();

  void emitEvent(String event);
}
