package com.dv027.aiot.model;

import java.time.ZonedDateTime;

/**
 * Represents SensorData.
 */
public class SensorDataModel {
    private float humidity;
    private float temperature;
    private ZonedDateTime time;

    /**
     * Creates a new instance of SensorDataModel.
     * 
     * @param humidity    - Humidity value.
     * @param temperature - Temperature value.
     * @param time        - Timestamp.
     */
    public SensorDataModel(float humidity, float temperature, ZonedDateTime time) {
        this.humidity = humidity;
        this.temperature = temperature;
        this.time = time;
    }

    /**
     * Creates a new instance of SensorDataModel.
     * 
     * @param humidity    - Humidity value.
     * @param temperature - Temperature value.
     */
    public SensorDataModel(float humidity, float temperature) {
        this.humidity = humidity;
        this.temperature = temperature;
    }

    /**
     * Returns the stored humidity.
     * 
     * @return float
     */
    public float getHumidity() {
        return this.humidity;
    }

    /**
     * Returns the stored temperature.
     * 
     * @return float
     */
    public float getTemperature() {
        return this.temperature;
    }

    /**
     * Returns the stored time.
     * 
     * @return ZonedDateTime
     */
    public ZonedDateTime getTime() {
        return this.time;
    }

    /**
     * Returns a string representation of the SensorData.
     * 
     * @return String - The string representation.
     */
    @Override
    public String toString() {
        return "Temperature: " + Float.toString(temperature) + ", Humidity: " + Float.toString(humidity);
    }
}
