package com.dv027.aiot.config;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.dv027.aiot.config.events.MqttConnectionFailedEvent;
import com.dv027.aiot.config.events.MqttConnectionSuccessEvent;

import jakarta.annotation.PreDestroy;

/**
 * Class that sets up the MqttClient and connects it to the provided mqtt
 * server.
 */
@Configuration
public class MqttConfig implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private Environment environment;

  private IMqttAsyncClient client;

  /**
   * Event listener for when application is ready (objects are wired up and
   * created). Important
   * to use when trying to connect to mqtt so that event listeners are ready for
   * events.
   * 
   * @param event - The application ready event that the method is listening to.
   */
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    try {
      this.connectAndPublishEvent();
    } catch (Exception e) {
      System.err.println("Failed to connect to MQTT broker: " + e.getMessage());
    }
  }

  /**
   * Method for creating a IMqttAsyncClient.
   * 
   * @return IMqttAsyncClient - The created IMqttAsyncClient.
   */
  @Bean
  IMqttAsyncClient mqttClient() {
    String url = environment.getRequiredProperty("mqtt-url");
    String publisherId = "spring_server" + MqttClient.generateClientId();
    try {
      this.client = new MqttAsyncClient(url, publisherId);
    } catch (Exception e) {
      System.err.println("Failed to create MQTT client: " + e.getMessage());
    }
    return client;
  }

  /**
   * Method for connecting to the Mqtt server and publish failure/success event.
   * 
   * @throws Exception
   */
  private void connectAndPublishEvent() throws Exception {
    String username = environment.getRequiredProperty("mqtt-username");
    String password = environment.getRequiredProperty("mqtt-key");

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    options.setUserName(username);
    options.setPassword(password.toCharArray());

    client.connect(options, null, new IMqttActionListener() {
      @Override
      public void onSuccess(IMqttToken asyncActionToken) {
        eventPublisher.publishEvent(new MqttConnectionSuccessEvent(this, client));
      }

      @Override
      public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        System.out.println("Connection failed!");
        System.out.println(exception.getMessage());
        eventPublisher.publishEvent(new MqttConnectionFailedEvent(this, exception));
      }
    });

  }

  /**
   * Method for disconnecting the mqtt client connection.
   * 
   * @throws Exception
   */
  @PreDestroy
  public void cleanUp() throws Exception {
    if (client != null) {
      if (client.isConnected()) {
        try {
          System.out.println("MQTT client is still connected..");
          System.out.println("Trying to disconnect client..");
          client.disconnect().waitForCompletion();
          System.out.println("MQTT client disconnected!");
        } catch (Exception e) {
          System.out.println("Failed to disconnect MQTT client: " + e.getMessage());
          throw e;
        }
      } else {
        System.out.println("MQTT client allready disconnected");
      }
      client.close();
      System.out.println("MQTT client closed successfully");
    }
  }

}
