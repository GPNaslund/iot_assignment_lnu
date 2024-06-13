package com.dv027.aiot.config.events;

import org.springframework.context.ApplicationEvent;

/**
 * An application event representing a failed connection to a MQTT server.
 */
public class MqttConnectionFailedEvent extends ApplicationEvent {
  private Throwable exception;

  /**
   * Creates a new instance of MqttConnectionFailedEvent.
   * 
   * @param source    - The source object that is emitting the event.
   * @param exception - Associated exception for the failed event.
   */
  public MqttConnectionFailedEvent(Object source, Throwable exception) {
    super(source);
    this.exception = exception;
  }

  /**
   * Method for getting the stored exception.
   * 
   * @return Throwable
   */
  public Throwable getException() {
    return this.exception;
  }

}
