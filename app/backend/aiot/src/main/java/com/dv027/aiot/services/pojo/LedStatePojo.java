package com.dv027.aiot.services.pojo;

/**
 * POJO for LedState.
 */
public class LedStatePojo {
    private String state;

    /**
     * Creates a new instance of LedStatePojo.
     * 
     * @param state - The state to store.
     */
    public LedStatePojo(String state) {
        this.state = state;
    }

    /**
     * Returns stored state field.
     * 
     * @return String
     */
    public String getState() {
        return this.state;
    }
}
