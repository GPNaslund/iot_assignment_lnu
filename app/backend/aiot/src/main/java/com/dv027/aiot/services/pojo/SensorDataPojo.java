package com.dv027.aiot.services.pojo;

import java.time.ZonedDateTime;

/**
 * Represents SensorData.
 */
public class SensorDataPojo {
    public float temperature;
    public float humidity;
    public ZonedDateTime time;

    /**
     * Creates a new instance of SensorDataPojo.
     * Default constructor for unmarshalling responses.
     */
    public SensorDataPojo() {
    }

    /**
     * Creates a new instance of SensorDataPojo.
     * 
     * @param temperature - Temperature measurement.
     * @param humidity    - Humidity measurement.
     * @param time        - Time stamp.
     */
    public SensorDataPojo(float temperature, float humidity, ZonedDateTime time) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.time = time;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "Time: " + time.toString() + ", temp: " + Float.toString(temperature) + ", humidity: "
                + Float.toString(humidity) + ".";
    }

}
