package com.dv027.aiot.repository.interfaces;

import com.dv027.aiot.model.LedStateModel;
import com.dv027.aiot.model.enums.LedState;

import reactor.core.publisher.Mono;

public interface LedRepository {
    void setLedState(LedState state) throws Exception;

    Mono<LedStateModel> getLedState();
}
