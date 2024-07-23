package com.demo.weather_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.fixtures.SensorDataFixture;
import com.demo.weather_service.response.ApiResponse;
import com.demo.weather_service.response.SensorDataResponse;
import com.demo.weather_service.response.SensorDatasResponse;
import com.demo.weather_service.response.StatisticsResponse;
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

    ResponseEntity<SensorDataResponse> expected = ResponseEntity.ok().body(new SensorDataResponse(SensorDataFixture.getSensorData()));
    assertEquals(expected,
        responseService.createLatestStateResponse(SensorDataFixture.getSensorData()));
  }

  @Test
  void ShouldReturnOkResponseWhenSensorDataMapContainsValidSensorData() {
    Map<String, SensorData> sensorDataMap = SensorDataFixture.getSensorDataMap();
    ResponseEntity<ApiResponse> expected = ResponseEntity.ok(new SensorDatasResponse(sensorDataMap));
    assertEquals(expected, responseService.createLatestStatesResponse(sensorDataMap));
  }

  @Test
  void whenSensorDataMapContainsValidAndInvalidShouldReturnMultiStatusResponse() {
    ResponseEntity<ApiResponse> expected =
        ResponseEntity.status(HttpStatus.MULTI_STATUS)
            .body(new SensorDatasResponse(SensorDataFixture.getValidAndInvalidSensorDataMap()));

    assertEquals(expected, responseService.createLatestStatesResponse(
        SensorDataFixture.getValidAndInvalidSensorDataMap()));
  }

  @Test
  void whenNoSensorDataIsPresentShouldReturnNotFoundResponse() {
    assertEquals(ResponseEntity.notFound().build(),
        responseService.createLatestStatesResponse(Map.of()));
  }

  @Test
  void whenNoStatisticsPresentShouldReturnNotFoundResponse() {
    assertEquals(ResponseEntity.notFound().build(),
        responseService.createStatisticsResponse(Map.of()));
  }

  @Test
  void whenStatisticsContainsInvalidAndValidStatisticsShouldReturnMultiStatusResponse() {
    Map<String, Double> statisticValues = StatisticsFixture.getValidAndInvalidStatistics();
    Map<String, Map<String, Double>> sensorStatistics = Map.of("sensor-1", statisticValues);
    assertEquals(ResponseEntity.status(HttpStatus.MULTI_STATUS).body(new StatisticsResponse(sensorStatistics)),
        responseService.createStatisticsResponse(sensorStatistics));
  }

  @Test
  void whenStatisticsContainsValidStatisticsShouldReturnOkResponse() {
    Map<String, Double> statisticValues = StatisticsFixture.getValidStatistics();
    Map<String, Map<String, Double>> sensorStatistics = Map.of("sensor-1", statisticValues);
    assertEquals(ResponseEntity.ok().body(new StatisticsResponse(sensorStatistics)),
        responseService.createStatisticsResponse(sensorStatistics));
  }

}