package com.dv027.aiot.services.interfaces;

import reactor.core.publisher.Flux;

public interface MqttSubscribeService {
  Flux<String> streamMessages();
}
