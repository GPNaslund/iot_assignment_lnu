package com.dv027.aiot.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.InfluxDBClientReactiveFactory;

/**
 * Class with methods for getting a configured InfluxDBClient.
 */
@Configuration
public class InfluxDBConfig {
  private Environment environment;
  private String org;
  private char[] token;
  private String url;

  /**
   * Creates a new instance of InfluxDBConfig.
   * 
   * @param env - The Environment instance.
   */
  @Autowired
  public InfluxDBConfig(Environment env) {
    this.validateEnvironmentVariables(env);
    this.environment = env;
    this.org = environment.getRequiredProperty("influx-org");
    this.token = environment.getRequiredProperty("influx-token").toCharArray();
    this.url = environment.getRequiredProperty("influx-url");
  }

  /**
   * Creates and returns a InfluxDBClientReactive instance.
   * 
   * @return InfluxDBClientReactive - The created InfluxDBClientReactive instance.
   */
  @Bean
  public InfluxDBClientReactive getClient() {
    InfluxDBClientReactive client = InfluxDBClientReactiveFactory.create(
        this.url,
        this.token,
        this.org);

    return client;
  }

  /**
   * Helper method for validating the environment variables.
   * 
   * @param environment
   */
  private void validateEnvironmentVariables(Environment environment) {
    if (environment == null) {
      throw new IllegalArgumentException("Environment class is null");
    }
    if (environment.getRequiredProperty("influx-org").isEmpty()) {
      throw new IllegalArgumentException("Influx organisation variable is empty");
    }
    if (environment.getRequiredProperty("influx-token").isEmpty()) {
      throw new IllegalArgumentException("Influx token variable is empty");
    }
    if (environment.getRequiredProperty("influx-url").isEmpty()) {
      throw new IllegalArgumentException("Influx url variable is empty");
    }
  }

}
