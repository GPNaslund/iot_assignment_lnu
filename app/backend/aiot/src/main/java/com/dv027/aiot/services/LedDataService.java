package com.dv027.aiot.services;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.dv027.aiot.model.LedStateModel;
import com.dv027.aiot.model.enums.LedState;
import com.dv027.aiot.repository.interfaces.LedRepository;
import com.dv027.aiot.services.interfaces.LedService;
import com.dv027.aiot.services.pojo.LedStatePojo;

import reactor.core.publisher.Mono;

/**
 * Class with methods for interacting with LedState data.
 */
@Service
public class LedDataService implements LedService {
  private IMqttAsyncClient mqttClient;
  private Environment environment;
  private final String ledFeed;
  private LedRepository repostiory;

  /**
   * Creates a new instance of LedControlMqttService.
   * 
   * @param client     - IMqttAsyncClient used to publish to feed.
   * @param env        - Environment instance for getting environment variable
   *                   values.
   * @param repository - Instance implementing the LedRepository interface.
   */
  @Autowired
  public LedDataService(IMqttAsyncClient client, Environment env, LedRepository repository) {
    this.mqttClient = client;
    this.environment = env;
    this.ledFeed = environment.getRequiredProperty("led-feed");
    this.repostiory = repository;
  }

  /**
   * Method for publishing provided message to led mqtt feed.
   * 
   * @param message - The message to publish.
   * @throws Exception
   */
  public void publishLedState(String message) throws Exception {
    if (!mqttClient.isConnected()) {
      throw new IllegalStateException("MQTT client is not connected");
    }
    try {
      LedState state = LedState.valueOf(message);
      this.repostiory.setLedState(state);
      MqttMessage mqttMessage = new MqttMessage(message.getBytes());
      mqttMessage.setQos(1);
      mqttMessage.setRetained(false);
      mqttClient.publish(ledFeed, mqttMessage);
    } catch (MqttException e) {
      System.out.println("Exception thrown on mqtt publishing: " + e.getMessage());
      throw new MqttException(e.getReasonCode());
    }
  }

  /**
   * Gets the latest stored LedState.
   * 
   * @return Flux<LedStatePojo> - The result of the query.
   */
  public Mono<LedStatePojo> getLedState() {
    return this.repostiory.getLedState().map(this::convertToPojo);
  }

  /**
   * Helper method for converting LedStateModel to LedStatePojo.
   * 
   * @param model - The model to create a pojo from.
   * @return LedStatePojo - The created pojo.
   */
  private LedStatePojo convertToPojo(LedStateModel model) {
    return new LedStatePojo(model.getState().getState());
  }

}
