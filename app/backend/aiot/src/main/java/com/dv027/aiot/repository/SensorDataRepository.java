package com.dv027.aiot.repository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.reactivestreams.Publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.dv027.aiot.model.SensorDataModel;
import com.dv027.aiot.repository.interfaces.SensorRepository;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.QueryReactiveApi;
import com.influxdb.client.reactive.WriteReactiveApi;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;

import jakarta.annotation.PreDestroy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class with methods for querying an influx db for sensor data.
 */
@Repository
public class SensorDataRepository implements SensorRepository {
  private InfluxDBClientReactive dbClient;
  private String shortTermBucket;
  private String longTermBucket;
  private String org;
  private String zoneId;

  /**
   * Creates a new instance of SensorDataRepository.
   * 
   * @param db  - InfluxDBClientReactive instance.
   * @param env - Environment instance.
   */
  @Autowired
  public SensorDataRepository(InfluxDBClientReactive db, Environment env) {
    this.dbClient = db;
    this.shortTermBucket = env.getRequiredProperty("influx-shortterm-bucket");
    this.longTermBucket = env.getRequiredProperty("influx-longterm-bucket");
    this.org = env.getRequiredProperty("influx-org");
    this.zoneId = env.getRequiredProperty("zone-id");
  }

  /**
   * Method for writing SensorData to influx database.
   * 
   * @param data - The SensorDataModel to get data from to write to database.
   * @throws Exception
   */
  public void writeData(SensorDataModel data) throws Exception {
    try {
      Point dataPoint = Point
          .measurement("dht22")
          .addField("temperature", data.getTemperature())
          .addField("humidity", data.getHumidity())
          .time(Instant.now(), WritePrecision.S);

      Mono<WriteReactiveApi.Success> result = Mono.from(dbClient.getWriteReactiveApi().writePoint(shortTermBucket,
          org,
          WritePrecision.S,
          dataPoint));
      result
          .doOnError(e -> System.out.println("Failed to store datapoint: " + e.getMessage()))
          .block();
    } catch (InfluxException e) {
      throw new Exception("Failed to save data sensor data", e);
    }
  }

  /**
   * Method for getting sensor data from the short term bucket.
   * 
   * @param from - Instant representing the start of the query timespan.
   * @param to   - Instant representing the end of the query timespan.
   * @return Flux<SensorDataModel> - The query result.
   */
  public Flux<SensorDataModel> getShortTermData(Instant from, Instant to) {
    return getSensorData(shortTermBucket, from, to);
  }

  /**
   * Method for getting sensor data from the long term bucket.
   * 
   * @param from - Instant representing the start of the query timespan.
   * @param to   - Instant representing the end of the query timespan.
   * @return Flux<SensorDataModel> - The query result.
   */
  public Flux<SensorDataModel> getLongTermData(Instant from, Instant to) {
    return getSensorData(longTermBucket, from, to);
  }

  /**
   * Private helper method for querying the influx database.
   * 
   * @param bucket - The name of the bucket to query from.
   * @param from   - Instant representing the start of the timespan of the query.
   * @param to     - Instant representing the end of the timespan of the query.
   * @return Flux<SensorDataModel> - The query result.
   */
  private Flux<SensorDataModel> getSensorData(String bucket, Instant from, Instant to) {

    String flux = String.format(
        "from(bucket:\"%s\") |> range(start: %s, stop: %s)" +
            "|> filter(fn: (r) => r._measurement == \"dht22\")" +
            "|> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")",
        bucket,
        from.toString(),
        to.toString());

    QueryReactiveApi queryApi = dbClient.getQueryReactiveApi();
    Publisher<FluxRecord> query = queryApi.query(flux);
    return Flux.from(query)
        .map(record -> {
          Object temperatureValue = record.getValueByKey("temperature");
          Object humidityValue = record.getValueByKey("humidity");
          float temperature = (temperatureValue != null) ? ((Number) temperatureValue).floatValue() : 0.0f;
          float humidity = (humidityValue != null) ? ((Number) humidityValue).floatValue() : 0.0f;

          Instant utcTime = record.getTime();
          ZoneId zone = ZoneId.of(this.zoneId);
          ZonedDateTime swedenTime = utcTime.atZone(zone);

          return new SensorDataModel(humidity, temperature, swedenTime);
        })
        .onErrorResume(e -> {
          e.printStackTrace();
          return Flux.empty();
        });
  }

  /**
   * Method for closing the database client connection before removing the
   * instance.
   */
  @PreDestroy
  public void close() {
    if (this.dbClient != null) {
      this.dbClient.close();
    }
  }
}
