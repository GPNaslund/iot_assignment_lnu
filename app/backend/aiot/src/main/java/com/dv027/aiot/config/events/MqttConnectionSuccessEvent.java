package com.dv027.aiot.config.events;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.springframework.context.ApplicationEvent;

/**
 * An application event representing a successful connection to Mqtt server.
 */
public class MqttConnectionSuccessEvent extends ApplicationEvent {
  private IMqttAsyncClient client;

  /**
   * Creates a new instance of MqttConnectionSuccessEvent.
   * 
   * @param source - The source object that emitts the event.
   * @param client - The ImqttAsyncClient that is associated with the successful
   *               connection.
   */
  public MqttConnectionSuccessEvent(Object source, IMqttAsyncClient client) {
    super(source);
    this.client = client;
  }

  /**
   * Gets the stored IMqttAsyncClient.
   * 
   * @return IMqttAsyncClient
   */
  public IMqttAsyncClient getClient() {
    return client;
  }
}
