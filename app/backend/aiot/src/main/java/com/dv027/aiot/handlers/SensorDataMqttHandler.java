package com.dv027.aiot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dv027.aiot.services.interfaces.SensorService;
import com.dv027.aiot.services.interfaces.EventService;
import com.dv027.aiot.services.interfaces.MqttSubscribeService;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Flux;

/**
 * Class that sets up the message handling from the sensor data mqtt feed.
 */
@Component
public class SensorDataMqttHandler {
  private final SensorService dataService;
  private final Flux<String> messageFlux;
  private final EventService eventService;

  /**
   * Creates an instance of SensorDataMqttHandler.
   * 
   * @param mqttService  - Instance implementing the MqttSubscribeService
   *                     interface.
   * @param eventService - Instance implementing the EventService interface.
   * @param dataService  - Instance implementing the SensorService interface.
   */
  @Autowired
  public SensorDataMqttHandler(
      MqttSubscribeService mqttService,
      EventService eventService,
      SensorService dataService) {
    this.messageFlux = mqttService.streamMessages();
    this.eventService = eventService;
    this.dataService = dataService;
  }

  /**
   * Method for initializing the message handler after construct.
   */
  @PostConstruct
  public void init() {
    this.handleMessages();
  }

  /**
   * Sets up how to handle messages from the mqtt feed.
   */
  public void handleMessages() {
    this.messageFlux.subscribe(message -> {
      this.eventService.emitEvent(message);
      this.dataService.writeData(message);
    });
  }

}
