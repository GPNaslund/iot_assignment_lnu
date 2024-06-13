package com.dv027.mqtt.handler;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dv027.mqtt.service.interfaces.AuthService;

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Handler;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.codes.MqttSubAckReasonCode;

/**
 * Class with method for handling incoming connections.
 */
public class EndpointHandler implements Handler<MqttEndpoint> {
  private AuthService authService;

  // Defines the allowed topics for subscription/publishing.
  private final List<String> ALLOWED_TOPICS = List.of("gn222gq/feeds/sensor-data", "gn222gq/feeds/led-control");

  // Stores connections.
  private final Map<String, Set<MqttEndpoint>> topicSubscribers = new HashMap<>();

  /**
   * Creates a new EndpointHandler.
   *
   * @param authService - Class that implements AuthService interface.
   */
  public EndpointHandler(AuthService authService) {
    this.authService = authService;
  }

  /**
   * The main method for handling incoming requests. Defines the handlers to be
   * attached to the request.
   *
   * @param endpoint - MQTTEndpoint which represents an incoming request.
   */
  @Override
  public void handle(MqttEndpoint endpoint) {

    System.out.println("MQTT client [" + endpoint.clientIdentifier() + "] request to connect, clean session = "
        + endpoint.isCleanSession());

    // Authentication of incoming connection.
    boolean isAuthenticated = this.authService.authenticate(endpoint.auth());
    if (!isAuthenticated) {
      endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
      return;
    }

    endpoint.accept(false);
    System.out.println(
        "[username = " + endpoint.auth().getUsername() + ", password = " + endpoint.auth().getPassword() + "]");

    // Logic for handling subscribtion request from connection.
    endpoint.subscribeHandler(subscribe -> {
      // Checks if the topics requested for subscription are valid.
      boolean validSubRequest = subscribe.topicSubscriptions().stream()
          .allMatch(subscription -> ALLOWED_TOPICS.contains(subscription.topicName()));

      // Rejects the subscription request if any topic is not valid.
      if (!validSubRequest) {
        List<MqttSubAckReasonCode> reasonCodes = new ArrayList<>();
        for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
          reasonCodes.add(MqttSubAckReasonCode.UNSPECIFIED_ERROR);
        }
        endpoint.subscribeAcknowledge(subscribe.messageId(), reasonCodes, MqttProperties.NO_PROPERTIES);
      } else {
        // Accept and store connection/endpoint if topics are valid.
        List<MqttSubAckReasonCode> reasonCodes = new ArrayList<>();
        for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
          System.out.println("Subscription for " + s.topicName() + " with QoS " + s.qualityOfService());
          reasonCodes.add(MqttSubAckReasonCode.qosGranted(s.qualityOfService()));
          topicSubscribers.computeIfAbsent(s.topicName(), n -> new HashSet<>()).add(endpoint);
        }
        endpoint.subscribeAcknowledge(subscribe.messageId(), reasonCodes, MqttProperties.NO_PROPERTIES);
      }
    });

    // Handles unsubscriptions.
    endpoint.unsubscribeHandler(unsubscribe -> {
      // Checks if topics in unsubscribe are valid.
      boolean validUnsubRequest = unsubscribe.topics().stream().allMatch(unsub -> ALLOWED_TOPICS.contains(unsub));
      // Unsubscribe and remove connection/endpoint if topics are valid.
      if (validUnsubRequest) {
        for (String topic : unsubscribe.topics()) {
          Set<MqttEndpoint> subscribers = topicSubscribers.get(topic);
          if (subscribers != null) {
            subscribers.remove(endpoint);
            if (subscribers.isEmpty()) {
              topicSubscribers.remove(topic);
            }
          }
        }
        endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
      }
    });

    // Handles publishing of message to topic.
    endpoint.publishHandler(message -> {
      if (endpoint.isConnected()) {
        System.out.println("Just received message [" + message.payload().toString(Charset.defaultCharset())
            + "] with QoS [" + message.qosLevel() + "]");

        String topic = message.topicName();
        Set<MqttEndpoint> subscribers = topicSubscribers.get(topic);
        if (subscribers != null) {
          for (MqttEndpoint subscriber : subscribers) {
            try {
              System.out.println(
                  "Publishing message: " + message.payload() + "To: " + subscriber.toString() + "from topic: " + topic);
              subscriber.publish(topic, message.payload(), message.qosLevel(), message.isDup(),
                  message.isRetain());
            } catch (Exception e) {
              System.out.println("Exception occured: " + e.getMessage());
            }
          }
        }

        if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
          endpoint.publishAcknowledge(message.messageId());
        } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
          endpoint.publishReceived(message.messageId());
        }
      } else {
        System.err.println("Attempted to publish before the connection was fully established");
      }
    }).publishReleaseHandler(messageId -> {
      endpoint.publishReceived(messageId);
    });

    // Handles disconnecting of connection.
    endpoint.disconnectHandler(c -> {
      topicSubscribers.values().forEach(subscribers -> subscribers.remove(endpoint));
    });
  }
}
