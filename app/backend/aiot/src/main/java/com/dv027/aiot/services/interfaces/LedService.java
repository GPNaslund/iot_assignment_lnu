package com.dv027.aiot.services.interfaces;

import com.dv027.aiot.services.pojo.LedStatePojo;

import reactor.core.publisher.Mono;

public interface LedService {
  void publishLedState(String message) throws Exception;

  Mono<LedStatePojo> getLedState() throws Exception;
}
