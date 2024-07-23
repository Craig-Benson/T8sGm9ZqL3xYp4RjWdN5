package com.demo.weather_service.service;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.response.ApiResponse;
import com.demo.weather_service.response.InvalidResponse;
import com.demo.weather_service.response.SensorDataResponse;
import com.demo.weather_service.response.SensorDatasResponse;
import com.demo.weather_service.response.StatisticsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

  @Autowired
  ObjectMapper objectMapper;

  public ResponseEntity<SensorDataResponse> createLatestStateResponse(SensorData sensorData) {

    if (sensorData == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(new SensorDataResponse(sensorData));
  }

  public ResponseEntity<ApiResponse> createLatestStatesResponse(
      Map<String, SensorData> sensorDataMap) {

    int valid = 0;
    int invalid = 0;

    for (Map.Entry<String, SensorData> entry : sensorDataMap.entrySet()) {

      if (entry.getValue() == null) {
        invalid++;
      } else {
        valid++;
      }
    }
    if (valid > 0 && invalid > 0) {
      return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(new SensorDatasResponse(sensorDataMap));
    }

    if (valid == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(new SensorDatasResponse(sensorDataMap));

  }

  public ResponseEntity<ApiResponse> createStatisticsResponse(
      Map<String, Map<String, Double>> statistics) {

    int valid = 0;
    int invalid = 0;

    for (Map.Entry<String, Map<String, Double>> entry : statistics.entrySet()) {

      Map<String, Double> sensorStatistics = entry.getValue();
      for (String statistic : sensorStatistics.keySet()) {
        if (statistic.equals("invalid-metric")) {
          invalid++;
        } else {
          valid++;
        }
      }
    }
    if (valid > 0 && invalid > 0) {

      return ResponseEntity.status(HttpStatus.MULTI_STATUS)
          .body(new StatisticsResponse(statistics));
    }

    if (valid == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(new StatisticsResponse(statistics));

  }

  public ResponseEntity<ApiResponse> createInvalidDateResponse() {
    return ResponseEntity.badRequest().body(new InvalidResponse("Invalid date range"));
  }
}




