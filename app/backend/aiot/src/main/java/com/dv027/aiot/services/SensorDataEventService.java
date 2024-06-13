package com.dv027.aiot.services;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.dv027.aiot.services.interfaces.EventService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Class with methods related to LedData event and stream.
 */
@Service
public class SensorDataEventService implements EventService {
  private final Sinks.Many<String> sink;

  /**
   * Creates a new instance of SensorDataEventService.
   */
  public SensorDataEventService() {
    this.sink = Sinks.many().replay().limit(1);
  }

  /**
   * Method for getting event stream for SensorData.
   * 
   * @return Flux<String> - Flux that passes heartbeats and event messages.
   */
  @Override
  public Flux<String> getEventStream() {
    Flux<String> heartbeat = Flux.interval(Duration.ofSeconds(30))
        .map(tick -> "{\"heartbeat\": \"heartbeat\"}");

    return Flux.merge(this.sink.asFlux(), heartbeat);
  }

  /**
   * Method for emitting an event through the processor.
   * 
   * @param event
   */
  @Override
  public void emitEvent(String event) {
    System.out.println("Emitting event: " + event);
    this.sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
  }
}
