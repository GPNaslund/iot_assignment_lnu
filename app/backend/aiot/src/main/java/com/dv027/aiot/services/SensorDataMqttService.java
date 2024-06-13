package com.dv027.aiot.services;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dv027.aiot.config.events.MqttConnectionSuccessEvent;
import com.dv027.aiot.services.interfaces.MqttSubscribeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Method for interacting with the IMqttAsyncClient to subscribe to the sensor
 * data feed.
 */
@Service
public class SensorDataMqttService implements MqttSubscribeService {

  private final IMqttAsyncClient mqttClient;
  private final Sinks.Many<String> messageSink = Sinks.many().multicast().onBackpressureBuffer();

  @Autowired
  private Environment environment;

  /**
   * Creates a new instance of SensorDataMqttService.
   * 
   * @param mqttClient - ImqttAsyncClient used to set up subscription.
   */
  @Autowired
  public SensorDataMqttService(IMqttAsyncClient mqttClient) {
    this.mqttClient = mqttClient;
  }

  /**
   * Sets up topic subscription after MqttConnectionSuccessEvent is triggered.
   * 
   * @param event - The event the method is listening for.
   */
  @EventListener
  public void handleConnectionSuccess(MqttConnectionSuccessEvent event) {
    subscribeToTopic();
  }

  /**
   * Helper method for setting up the subscription to the sensor-data-feed.
   */
  private void subscribeToTopic() {
    String topic = environment.getRequiredProperty("sensor-data-feed");
    try {
      mqttClient.subscribe(topic, 1, (tpc, msg) -> {
        String message = new String(msg.getPayload());
        System.out.println("MQTT Service got message: " + message);
        messageSink.tryEmitNext(message);
      });
    } catch (Exception e) {
      System.out.println("Failed to subscribe to topic: " + e.getMessage());
      messageSink.tryEmitError(e);
    }
  }

  /**
   * Method for getting the subscribtion tied messageSink for streaming of
   * messages.
   * 
   * @return Flux<String> - The message sink as a flux.
   */
  public Flux<String> streamMessages() {
    return messageSink.asFlux();
  }

}
