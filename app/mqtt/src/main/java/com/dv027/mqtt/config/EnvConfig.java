package com.dv027.mqtt.config;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Class with method for getting a environment config store.
 */
public class EnvConfig {
  /**
   * Sets up the environment store for getting environment variables.
   *
   * @return ConfigStoreOptions
   */
  public ConfigStoreOptions getEnvOptions() {
    JsonArray envVariables = new JsonArray();
    envVariables.add("VERTX_MQTT_USERNAME");
    envVariables.add("VERTX_MQTT_PASSWORD");

    ConfigStoreOptions envConfig = new ConfigStoreOptions()
        .setType("env")
        .setConfig(new JsonObject().put("keys", envVariables));

    return envConfig;
  }
}
