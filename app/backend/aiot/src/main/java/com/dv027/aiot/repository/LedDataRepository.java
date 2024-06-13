package com.dv027.aiot.repository;

import java.time.Instant;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.dv027.aiot.model.LedStateModel;
import com.dv027.aiot.model.enums.LedState;
import com.dv027.aiot.repository.interfaces.LedRepository;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.QueryReactiveApi;
import com.influxdb.client.reactive.WriteReactiveApi;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;

import reactor.core.publisher.Mono;

/**
 * Repository class for the stored LED state.
 */
@Repository
public class LedDataRepository implements LedRepository {
  private InfluxDBClientReactive dbClient;
  private String ledStateBucket;
  private String influxOrg;

  /**
   * Creates a new instance of LedStateRepository.
   * 
   * @param db  - InfluxDBClientReactive for the stored LED state.
   * @param env - Environment instance for getting environment variables.
   */
  @Autowired
  public LedDataRepository(InfluxDBClientReactive db, Environment env) {
    this.dbClient = db;
    this.ledStateBucket = env.getRequiredProperty("influx-led-bucket");
    this.influxOrg = env.getRequiredProperty("influx-org");
  }

  /**
   * Method for storing a new LedState in the database.
   * 
   * @param state - The LedState to store.
   * @throws Exception
   */
  @Override
  public void setLedState(LedState state) throws Exception {
    try {
      Point dataPoint = Point
          .measurement("led-state")
          .addField("state", state.getState())
          .time(Instant.now(), WritePrecision.S);
      Mono<WriteReactiveApi.Success> result = Mono.from(
          dbClient.getWriteReactiveApi().writePoint(ledStateBucket, influxOrg, WritePrecision.S, dataPoint));

      result
          .doOnError(e -> System.out.println("Failed to store led state: " + e.getMessage()))
          .subscribe();
    } catch (InfluxException e) {
      throw new Exception("Failed to store led state to db", e);
    }
  }

  /**
   * Returns the most recent stored LedState from the database.
   * 
   * @return Flux<LedStateModel> - The stored LedStateModel.
   */
  @Override
  public Mono<LedStateModel> getLedState() {
    String flux = String.format(
        "from(bucket: \"%s\") |> range(start: -7d) |> filter(fn: (r) => r._measurement == \"led-state\") |> last()",
        ledStateBucket);
    QueryReactiveApi queryApi = dbClient.getQueryReactiveApi();
    Publisher<FluxRecord> query = queryApi.query(flux);
    return Mono.from(query)
        .map(record -> {
          Object stateValue = record.getValue();
          String stateStr = (stateValue != null) ? (String) stateValue : "OFF";
          return new LedStateModel(LedState.valueOf(stateStr));
        })
        .onErrorResume(e -> {
          e.printStackTrace();
          return Mono.empty();
        });
  }

}
