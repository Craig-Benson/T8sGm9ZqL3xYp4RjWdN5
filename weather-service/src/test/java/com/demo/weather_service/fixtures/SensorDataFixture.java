package com.demo.weather_service.fixtures;

import com.demo.weather_service.data.SensorData;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SensorDataFixture {


  private static final String SENSOR_ID = "valid-sensor-id";
  public static final LocalDate DATE = LocalDate.of(2024, 1, 1);
  private static final double TEMPERATURE = 10.0;
  private static final double HUMIDITY = 60.0;
  private static final double WIND_SPEED = 5.0;

  private static final LocalDate ALTERNATE_DATE = LocalDate.of(2024, 1, 2);
  private static final double ALTERNATE_TEMPERATURE = 5.0;
  private static final double ALTERNATE_HUMIDITY = 50.0;
  private static final double ALTERNATE_WIND_SPEED = 2.0;


  public static SensorData getSensorData(String sensorId, LocalDate date,
                                         Double temperature, Double humidity,
                                         Double windSpeed) {
    return SensorData.builder()
        .sensorId(sensorId)
        .date(date)
        .temperature(temperature)
        .humidity(humidity)
        .windSpeed(windSpeed)
        .build();
  }

  public static SensorData getSensorData() {
    return SensorData.builder()
        .sensorId(SENSOR_ID)
        .date(DATE)
        .temperature(TEMPERATURE)
        .humidity(HUMIDITY)
        .windSpeed(WIND_SPEED)
        .build();
  }


  public static SensorData getSensorDataForDb(long id, String sensorId, LocalDate date,
                                              double temperature, double humidity,
                                              double windSpeed) {

    return SensorData.builder()
        .id(id)
        .sensorId(sensorId)
        .date(date)
        .temperature(temperature)
        .humidity(humidity)
        .windSpeed(windSpeed)
        .build();

  }

  public static SensorData getSensorDataForDb(long id) {
    return getSensorDataForDb(id, SENSOR_ID);
  }

  public static SensorData getSensorDataForDb(long id, String sensorId) {

    return SensorData.builder()
        .id(id)
        .sensorId(sensorId)
        .date(DATE)
        .temperature(TEMPERATURE)
        .humidity(HUMIDITY)
        .windSpeed(WIND_SPEED)
        .build();

  }

  public static SensorData getSensorDataForDb(long id, String sensorId, double temperature) {

    return SensorData.builder()
        .id(id)
        .sensorId(sensorId)
        .date(DATE)
        .temperature(temperature)
        .humidity(HUMIDITY)
        .windSpeed(WIND_SPEED)
        .build();

  }

  public static Map<String, SensorData> getSensorDataMap() {
    return Map.of("sensor-1", SensorDataFixture.getSensorData(),
        "sensor-2", SensorDataFixture.getSensorData());
  }

  public static Map<String, SensorData> getValidAndInvalidSensorDataMap() {
    Map<String, SensorData> sensorDataMap = new HashMap<>();
    sensorDataMap.put("sensor-1", SensorDataFixture.getSensorData());
    sensorDataMap.put("invalid", null);
    return sensorDataMap;
  }
}
