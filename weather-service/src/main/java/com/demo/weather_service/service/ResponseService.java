package com.demo.weather_service.service;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.response.ApiResponse;
import com.demo.weather_service.response.InvalidResponse;
import com.demo.weather_service.response.SensorDataResponse;
import com.demo.weather_service.response.SensorDatasResponse;
import com.demo.weather_service.response.StatisticsResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

  Logger logger = LoggerFactory.getLogger(ResponseService.class);

  public ResponseEntity<SensorDataResponse> createLatestStateResponse(SensorData sensorData) {

    if (sensorData == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(new SensorDataResponse(sensorData));
  }

  public ResponseEntity<ApiResponse> createLatestStatesResponse(
      Map<String, SensorData> sensorDataMap) {

    int validSensorData = 0;
    int invalidSensorData = 0;

    for (Map.Entry<String, SensorData> sensorData : sensorDataMap.entrySet()) {

      if (sensorData.getValue() == null) {
        logger.info("msg={} sensor_id={}", "Invalid sensor data present", sensorData.getKey());
        invalidSensorData++;
      } else {
        validSensorData++;
      }
    }
    if (validSensorData > 0 && invalidSensorData > 0) {
      return ResponseEntity.status(HttpStatus.MULTI_STATUS)
          .body(new SensorDatasResponse(sensorDataMap));
    }

    if (validSensorData == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(new SensorDatasResponse(sensorDataMap));

  }

  public ResponseEntity<ApiResponse> createStatisticsResponse(
      Map<String, Map<String, Double>> sensorDatas) {

    int validMetric = 0;
    int invalidMetric = 0;

    for (Map.Entry<String, Map<String, Double>> sensorData : sensorDatas.entrySet()) {

      Map<String, Double> sensorMetrics = sensorData.getValue();

      for (String metric : sensorMetrics.keySet()) {
        if (metric.equals("invalid-metric")) {
          logger.warn("msg={}, metric={}", "Invalid metric present", metric);
          invalidMetric++;
        } else {
          validMetric++;
        }
      }
    }
    if (validMetric > 0 && invalidMetric > 0) {

      return ResponseEntity.status(HttpStatus.MULTI_STATUS)
          .body(new StatisticsResponse(sensorDatas));
    }

    if (validMetric == 0) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(new StatisticsResponse(sensorDatas));

  }

  public ResponseEntity<ApiResponse> createInvalidDateResponse() {
    return ResponseEntity.badRequest().body(new InvalidResponse("Invalid date range"));
  }
}




