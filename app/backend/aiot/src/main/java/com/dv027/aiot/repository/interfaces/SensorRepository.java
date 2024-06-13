package com.dv027.aiot.repository.interfaces;

import java.time.Instant;

import com.dv027.aiot.model.SensorDataModel;

import reactor.core.publisher.Flux;

public interface SensorRepository {
    void writeData(SensorDataModel data) throws Exception;

    Flux<SensorDataModel> getShortTermData(Instant from, Instant to);

    Flux<SensorDataModel> getLongTermData(Instant from, Instant to);
}
