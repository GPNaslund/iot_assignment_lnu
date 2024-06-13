package com.dv027.mqtt;

import com.dv027.mqtt.config.EnvConfig;
import com.dv027.mqtt.handler.EndpointHandler;
import com.dv027.mqtt.service.AuthServiceImpl;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

/**
 * The main entry point for the MQTT server.
 */
public class MainVerticle extends AbstractVerticle {

  /**
   * Starts the MQTT server.
   *
   * @param startPromise - Promise provided when starting the applcation.
   * @throws Exception
   */
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    EnvConfig envConfig = new EnvConfig();
    ConfigStoreOptions envOptions = envConfig.getEnvOptions();
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(envOptions);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig().onComplete(json -> {
      JsonObject envVariables = json.result();
      String storedUsername = envVariables.getString("VERTX_MQTT_USERNAME");
      String storedPassword = envVariables.getString("VERTX_MQTT_PASSWORD");

      // Exit application if no username and/or password is provided in environment.
      if (storedUsername == null || storedPassword == null) {
        System.out.println("No username and/or password provided in environment.");
        System.exit(1);
      }

      MqttServerOptions serverOptions = new MqttServerOptions()
          .setPort(1883)
          .setHost("0.0.0.0");

      MqttServer mqttServer = MqttServer.create(vertx, serverOptions);
      AuthServiceImpl authService = new AuthServiceImpl(storedUsername, storedPassword);
      EndpointHandler handler = new EndpointHandler(authService);

      // Callback for handling a request to the MQTT server.
      mqttServer.endpointHandler(handler::handle)
          .listen()
          .onComplete(ar -> {
            if (ar.succeeded()) {
              System.out.println("MQTT server is listening on port " + ar.result().actualPort());
            } else {
              System.out.println("Error on starting the server");
              ar.cause().printStackTrace();
            }
          });
    });
  }
}
