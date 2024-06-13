package com.dv027.mqtt.service.interfaces;

import io.vertx.mqtt.MqttAuth;

public interface AuthService {
    boolean authenticate(MqttAuth auth);
}
