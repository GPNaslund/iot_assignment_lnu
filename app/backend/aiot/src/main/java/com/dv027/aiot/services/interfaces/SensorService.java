package com.dv027.aiot.services.interfaces;

import java.time.Instant;

import com.dv027.aiot.services.pojo.SensorDataPojo;

import reactor.core.publisher.Flux;

public interface SensorService {
    void writeData(String sensorData);

    Flux<SensorDataPojo> getShortTermData(Instant from, Instant to);

    Flux<SensorDataPojo> getLongTermData(Instant from, Instant to);
}
