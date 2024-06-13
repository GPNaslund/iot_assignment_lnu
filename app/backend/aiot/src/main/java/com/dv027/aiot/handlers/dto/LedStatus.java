package com.dv027.aiot.handlers.dto;

/**
 * Represents a LED status.
 */
public class LedStatus {
    private String state;

    /**
     * Creates an instance of LedStatus. Default
     * constructor for unmarshalling a response object.
     */
    public LedStatus() {
    }

    /**
     * Creates an instance of LedStatus.
     * 
     * @param value - The led status value.
     */
    public LedStatus(String value) {
        this.state = value;
    }

    /**
     * Returns the status variable.
     * 
     * @return String
     */
    public String getState() {
        return this.state;
    }
}
