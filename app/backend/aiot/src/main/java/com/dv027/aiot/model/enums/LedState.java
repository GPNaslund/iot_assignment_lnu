package com.dv027.aiot.model.enums;

public enum LedState {
    ON("ON"),
    OFF("OFF");

    private final String state;

    LedState(String value) {
        this.state = value;
    }

    public String getState() {
        return this.state;
    }
}
