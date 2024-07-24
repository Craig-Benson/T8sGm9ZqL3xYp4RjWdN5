package com.demo.weather_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.fixtures.SensorDataFixture;
import com.demo.weather_service.service.mapper.StatisticsMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StatisticsMapperTest {

  StatisticsMapper statisticsMapper = new StatisticsMapper();

  @Test
  void shouldMapAverageForAllValidMetrics() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("temperature", 7.5, "humidity", 55.0, "windspeed", 3.5);
    assertEquals(expected, statisticsMapper.mapAverageStatistic(sensorDatas,
        List.of("temperature", "humidity", "windspeed")));

  }

  @Test
  void shouldMapMaxForAllValidMetrics() {
      List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
          SensorDataFixture.getSensorDataOneAlternate());

      Map<String, Double> expected = Map.of("temperature", 10.0, "humidity", 60.0, "windspeed", 5.0);
      assertEquals(expected, statisticsMapper.mapMaxStatistic(sensorDatas,
          List.of("temperature", "humidity", "windspeed")));
  }

  @Test
  void shouldMapMinForAllValidMetrics() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("temperature", 5.0, "humidity", 50.0, "windspeed", 2.0);
    assertEquals(expected, statisticsMapper.mapMinStatistic(sensorDatas,
        List.of("temperature", "humidity", "windspeed")));
  }

  @Test
  void shouldMapSumForAllValidMetrics() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("temperature", 15.0, "humidity", 110.0, "windspeed", 7.0);
    assertEquals(expected, statisticsMapper.mapSumStatistic(sensorDatas,
        List.of("temperature", "humidity", "windspeed")));
  }

  @Test
  void shouldMapInvalidWhenInvalidAverageMetricPresent() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("invalid-metric", 0.0);
    assertEquals(expected, statisticsMapper.mapAverageStatistic(sensorDatas, List.of("invalid")));

  }

  @Test
  void shouldMapInvalidWhenInvalidMaxMetricPresent() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("invalid-metric", 0.0);
    assertEquals(expected,  statisticsMapper.mapMaxStatistic(sensorDatas,
        List.of("invalid")));
  }

  @Test
  void shouldMapInvalidWhenInvalidMinMetricPresent() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("invalid-metric", 0.0); assertEquals(expected, statisticsMapper.mapMinStatistic(sensorDatas,
        List.of("invalid")));
  }

  @Test
  void shouldMapInvalidWhenInvalidSumMetricPresent() {
    List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorDataOne(),
        SensorDataFixture.getSensorDataOneAlternate());

    Map<String, Double> expected = Map.of("invalid-metric", 0.0);
    assertEquals(expected, statisticsMapper.mapSumStatistic(sensorDatas,
        List.of("invalid")));
  }
}