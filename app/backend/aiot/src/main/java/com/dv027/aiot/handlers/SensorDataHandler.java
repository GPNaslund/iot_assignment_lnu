package com.dv027.aiot.handlers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dv027.aiot.handlers.interfaces.DataHandler;
import com.dv027.aiot.services.interfaces.SensorService;
import com.dv027.aiot.services.pojo.SensorDataPojo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class with handler methods for getting stored sensor data.
 */
@Component
public class SensorDataHandler implements DataHandler {
  private final SensorService service;

  /**
   * Creates a new instance of SensorDataHandler.
   * 
   * @param service - Instance that implements the SensorService interface.
   */
  @Autowired
  public SensorDataHandler(SensorService service) {
    this.service = service;
  }

  /**
   * Method for getting detailed short term sensor data.
   * 
   * @param request - The incoming server request.
   * @return Mono<ServerResponse> - The outgoing server response.
   */
  public Mono<ServerResponse> getShortTermData(ServerRequest request) {
    try {
      Instant start = this.createInstantFromQueryParam(request.queryParam("start"), true);
      Instant end = this.createInstantFromQueryParam(request.queryParam("end"), false);

      Flux<SensorDataPojo> result = this.service.getShortTermData(start, end);

      Mono<List<SensorDataPojo>> collectedList = result.collectList();
      return collectedList.flatMap(list -> {
        if (list.isEmpty()) {
          return ServerResponse.noContent().build();
        } else {
          return ServerResponse.ok().bodyValue(list);
        }
      })
          .onErrorResume(e -> {
            e.printStackTrace();
            return ServerResponse.status(500).bodyValue("Internal server error");
          });
    } catch (Exception e) {
      return ServerResponse.status(400).bodyValue("Bad request");
    }
  }

  /**
   * Method for getting aggregated long term sensor data.
   * 
   * @param request - The incoming server request.
   * @return Mono<ServerResponse> - The outgoing server response. - The outgoing
   *         server response.
   */
  public Mono<ServerResponse> getLongTermData(ServerRequest request) {
    ZonedDateTime now = ZonedDateTime.now();
    ZonedDateTime oneYearAgo = now.minus(1, ChronoUnit.YEARS);

    Flux<SensorDataPojo> result = this.service.getLongTermData(oneYearAgo.toInstant(), Instant.now());
    Mono<List<SensorDataPojo>> collectedList = result.collectList();
    return collectedList.flatMap(list -> {
      if (list.isEmpty()) {
        return ServerResponse.noContent().build();
      } else {
        return ServerResponse.ok().bodyValue(list);
      }
    })
        .onErrorResume(e -> {
          e.printStackTrace();
          return ServerResponse.status(500).bodyValue("Internal server error");
        });
  }

  /**
   * Helper method for creating Instant instance from query parameter.
   * 
   * @param queryParam - Optional query parameter
   * @param startOfDay - If the Instant instance should be from the start of the
   *                   provided date, or the end of the provided date.
   * @return Instant - The created Instant instance.
   * @throws Exception
   */
  private Instant createInstantFromQueryParam(Optional<String> queryParam, boolean startOfDay) throws Exception {
    if (!queryParam.isPresent()) {
      throw new IllegalArgumentException("No query param value");
    }
    String queryParamValue = queryParam.get();
    LocalDate localDate = LocalDate.parse(queryParamValue);
    if (startOfDay) {
      ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));
      return zonedDateTime.toInstant();
    } else {
      ZonedDateTime zonedDateTime = localDate.atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"));
      return zonedDateTime.toInstant();
    }
  }

}
