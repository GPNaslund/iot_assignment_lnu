package com.dv027.aiot.model;

import com.dv027.aiot.model.enums.LedState;

/**
 * Represents a LedState, has a LedState enum stored as state.
 */
public class LedStateModel {
    private LedState state;

    /**
     * Creates a new instance of LedStateModel.
     * 
     * @param state - The LedState to store.
     */
    public LedStateModel(LedState state) {
        this.state = state;
    }

    /**
     * Returns the state.
     * 
     * @return LedState
     */
    public LedState getState() {
        return this.state;
    }
}
