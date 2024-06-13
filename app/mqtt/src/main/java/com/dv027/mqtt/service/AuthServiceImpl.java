package com.dv027.mqtt.service;

import com.dv027.mqtt.service.interfaces.AuthService;

import io.vertx.mqtt.MqttAuth;

/**
 * Class for authenticating provided auth details from incoming requests.
 */
public class AuthServiceImpl implements AuthService {
    private String storedUsername;
    private String storedPassword;

    /**
     * Creates a new instance of AuthServiceImpl.
     * 
     * @param storedUsername - The username to check incoming requests against.
     * @param storedPassword - The password to check incoming requests against.
     */
    public AuthServiceImpl(String storedUsername, String storedPassword) {
        this.storedUsername = storedUsername;
        this.storedPassword = storedPassword;
    }

    /**
     * Method for authenticating provdided auth info.
     * 
     * @param auth - MQTTAuth object containing username and password.
     * @return boolean - Indicating if username and password matches.
     */
    public boolean authenticate(MqttAuth auth) {
        if (auth == null) {
            return false;
        }

        String providedUsername = auth.getUsername();
        String providedPassword = auth.getPassword();

        if (providedUsername.equals(storedUsername) && providedPassword.equals(storedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
