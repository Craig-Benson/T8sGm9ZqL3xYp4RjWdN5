package com.demo.weather_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.fixtures.SensorDataFixture;
import com.demo.weather_service.repository.SensorDataRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

public class SensorDataServiceTest {


  SensorDataRepository sensorDataRepository = mock(SensorDataRepository.class);

  @InjectMocks
  SensorDataService sensorDataService = new SensorDataService(sensorDataRepository);

  List<SensorData> sensorDatas = List.of(SensorDataFixture.getSensorData("sensor-1"),
      SensorDataFixture.getAlternateSensorData("sensor-1"));

  @Test
  public void shouldAverageTemperatureForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricAverageForSensor("sensor-1", "temperature",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getTemperature).sum() / sensorDatas.size();

    assertEquals(expected, actual);

  }

  @Test
  public void shouldAverageHumidityForASingleSensor() {
    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricAverageForSensor("sensor-1", "humidity",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getHumidity).sum() / sensorDatas.size();

    assertEquals(expected, actual);

  }

  @Test
  public void shouldAverageWindSpeedForASingleSensor() {
    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricAverageForSensor("sensor-1", "windSpeed",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getWindSpeed).sum() / sensorDatas.size();

    assertEquals(expected, actual);

  }


  @Test
  public void shouldSumTemperatureForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricSumForSensor("sensor-1", "temperature",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected = sensorDatas.stream().mapToDouble(SensorData::getTemperature).sum();
    assertEquals(expected, actual);

  }

  @Test
  public void shouldSumHumidityForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricSumForSensor("sensor-1", "humidity",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected = sensorDatas.stream().mapToDouble(SensorData::getHumidity).sum();
    assertEquals(expected, actual);

  }

  @Test
  public void shouldSumWindSpeedForASingleSensor() {
    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricSumForSensor("sensor-1", "windSpeed",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected = sensorDatas.stream().mapToDouble(SensorData::getWindSpeed).sum();
    assertEquals(expected, actual);

  }


  @Test
  public void shouldMinTemperatureForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricMinForSensor("sensor-1", "temperature",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getTemperature).min().getAsDouble();

    assertEquals(expected, actual);

  }

  @Test
  public void shouldMinHumidityForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricMinForSensor("sensor-1", "humidity",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected = sensorDatas.stream().mapToDouble(SensorData::getHumidity).min().getAsDouble();

    assertEquals(expected, actual);

  }

  @Test
  public void shouldMinWindSpeedForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricMinForSensor("sensor-1", "windSpeed",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getWindSpeed).min().getAsDouble();

    assertEquals(expected, actual);

  }

  @Test
  public void shouldMaxHumidityForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricMaxForSensor("sensor-1", "humidity",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected = sensorDatas.stream().mapToDouble(SensorData::getHumidity).max().getAsDouble();
    assertEquals(expected, actual);

  }


  @Test
  public void shouldMaxTemperatureForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricMaxForSensor("sensor-1", "temperature",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getTemperature).max().getAsDouble();
    assertEquals(expected, actual);

  }

  @Test
  public void shouldMaxWindSpeedForASingleSensor() {

    Mockito.when(
            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
        .thenReturn(sensorDatas);

    double actual =
        sensorDataService.getMetricMaxForSensor("sensor-1", "windSpeed",
            LocalDate.of(2024, 7, 16), LocalDate.of(2024, 7, 18));

    double expected =
        sensorDatas.stream().mapToDouble(SensorData::getWindSpeed).max().getAsDouble();
    assertEquals(expected, actual);

  }

//TODO: defaults .getAsDouble() validation, null, bad string


  @Test
  public void shouldThrowResponseStatusExceptionWhenNoResourceFound() {

//    Mockito.when(
//            sensorDataRepository.findSensorDataBySensorIdAndDateBetween(any(), any(), any()))
//        .thenReturn(sensorData);

    assertThrows(ResponseStatusException.class,
        () -> sensorDataService.getMetricMaxForSensor("sensor-1", "windSpeed",
            LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 2)));


  }
}