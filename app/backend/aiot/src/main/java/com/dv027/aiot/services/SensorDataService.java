package com.dv027.aiot.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dv027.aiot.model.SensorDataModel;
import com.dv027.aiot.repository.interfaces.SensorRepository;
import com.dv027.aiot.services.interfaces.SensorService;
import com.dv027.aiot.services.pojo.SensorDataPojo;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Service
public class SensorDataService implements SensorService {
  private SensorRepository repo;

  @Autowired
  public SensorDataService(SensorRepository repo) {
    this.repo = repo;
  }

  /**
   * Method for writing sensor data to the repository.
   * 
   * @param sensorData - The sensor data to be saved.
   */
  public void writeData(String sensorData) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      SensorDataPojo pojo = objectMapper.readValue(sensorData, SensorDataPojo.class);
      SensorDataModel model = new SensorDataModel(pojo.humidity, pojo.temperature);
      repo.writeData(model);
    } catch (Exception e) {
      System.out.println("Failed to convert sensor data string to POJO");
    }
  }

  /**
   * Method for getting the short term sensor data.
   * 
   * @param from - Instant that represents the start of the timespan of the query.
   * @param to   - Instant that represents the end of the timespan of the query.
   * @return Flux<SensorDataPojo> - The query result.
   */
  public Flux<SensorDataPojo> getShortTermData(Instant from, Instant to) {
    return this.repo.getShortTermData(from, to).map(this::convertToPojo);
  }

  /**
   * Method for getting the long term sensor data.
   * 
   * @param from - Instant that represents the start of the timespan of the query.
   * @param to   - Instant that represents the end of the timespan of the query.
   * @return Flux<SensorDataPojo> - The query result.
   */
  public Flux<SensorDataPojo> getLongTermData(Instant from, Instant to) {
    return this.repo.getLongTermData(from, to).map(this::convertToPojo);
  }

  /**
   * Helper method for converting SensorDataModel to SensorDataPojo.
   * 
   * @param model - The model to be used for converting to pojo.
   * @return SensorDataPojo - The created pojo.
   */
  private SensorDataPojo convertToPojo(SensorDataModel model) {
    return new SensorDataPojo(model.getTemperature(), model.getHumidity(), model.getTime());
  }
}
