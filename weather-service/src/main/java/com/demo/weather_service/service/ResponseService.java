package com.demo.weather_service.service;

import com.demo.weather_service.data.SensorData;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

  public ResponseEntity<SensorData> createLatestStateResponse(SensorData sensorData) {

    if (sensorData == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(sensorData);
  }

  public ResponseEntity<Map<String, SensorData>> createMultipleSensorDataResponse(
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
      return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(sensorDataMap);
    }

    if (valid == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(sensorDataMap);

  }

  public ResponseEntity<Map<String, Map<String, Double>>> createStatisticsResponse(
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
      return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(statistics);
    }

    if (valid == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(statistics);

  }


}




