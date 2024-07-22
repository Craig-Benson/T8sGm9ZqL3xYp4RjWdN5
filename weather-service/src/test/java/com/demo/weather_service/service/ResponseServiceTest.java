package com.demo.weather_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.fixtures.SensorDataFixture;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ResponseServiceTest {

  private final ResponseService responseService = new ResponseService();

  @Test
  void ShouldReturnNotFoundResponseWhenSensorDataIsNull() {
    assertEquals(ResponseEntity.notFound().build(),
        responseService.createLatestStateResponse(null));
  }

  @Test
  void ShouldReturnOkResponseWhenSensorDataIsValid() {
    ResponseEntity<SensorData> expected = ResponseEntity.ok(SensorDataFixture.getSensorData());
    assertEquals(expected, responseService.createLatestStateResponse(SensorDataFixture.getSensorData()));
  }

  @Test
  void ShouldReturnOkResponseWhenSensorDataMapContainsValidSensorData() {
    Map<String, SensorData> sensorDataMap = Map.of("sensor-1", SensorDataFixture.getSensorData(), "sensor-2", SensorDataFixture.getSensorData());
    ResponseEntity<Map<String, SensorData>> expected = ResponseEntity.ok(sensorDataMap);
    assertEquals(expected, responseService.createMultipleSensorDataResponse(sensorDataMap));
  }

  @Test
  void whenSensorDataMapIsValidShouldReturnOkResponse() {
    Map<String, SensorData> sensorDataMap = new HashMap<>();
    sensorDataMap.put("sensor-1", SensorDataFixture.getSensorData());
    sensorDataMap.put("invalid", null);
    ResponseEntity<Map<String, SensorData>> expected = ResponseEntity.status(HttpStatus.MULTI_STATUS).body(sensorDataMap);
    assertEquals(expected, responseService.createMultipleSensorDataResponse(sensorDataMap));
  }

  @Test
  void whenNoSensorDataIsPresentShouldReturnNotFoundResponse() {
    assertEquals(ResponseEntity.notFound().build(), responseService.createMultipleSensorDataResponse(Map.of()));
  }

  @Test
  void whenNoStatisticsPresentShouldReturnNotFoundResponse() {
    assertEquals(ResponseEntity.notFound().build(), responseService.createStatisticsResponse(Map.of()));
  }

  @Test
  void whenStatisticsContainsInvalidAndValidStatisticsShouldReturnMultiStatusResponse() {
    Map<String, Double> statisticValues = StatisticsFixture.getValidAndInvalidStatistics();
    Map<String, Map<String, Double>> sensorStatistics = Map.of("sensor-1", statisticValues);
    assertEquals(ResponseEntity.status(HttpStatus.MULTI_STATUS).body(sensorStatistics), responseService.createStatisticsResponse(sensorStatistics));
  }

  @Test
  void whenStatisticsContainsValidStatisticsShouldReturnOkResponse() {
    Map<String, Double> statisticValues = StatisticsFixture.getValidStatistics();
    Map<String, Map<String, Double>> sensorStatistics = Map.of("sensor-1", statisticValues);
    assertEquals(ResponseEntity.ok().body(sensorStatistics), responseService.createStatisticsResponse(sensorStatistics));
  }

}