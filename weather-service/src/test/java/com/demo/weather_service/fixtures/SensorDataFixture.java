package com.demo.weather_service.fixtures;

import com.demo.weather_service.data.SensorData;
import java.time.LocalDate;

public class SensorDataFixture {


  private static final String SENSOR_ID = "valid-sensor-id";
  private static final LocalDate DATE = LocalDate.of(2024, 1, 1);
  private static final double TEMPERATURE = 10.0;
  private static final double HUMIDITY = 60.0;
  private static final double WIND_SPEED = 5.0;

  private static final LocalDate ALTERNATE_DATE = LocalDate.of(2024, 1, 1);
  private static final double ALTERNATE_TEMPERATURE = 5.0;
  private static final double ALTERNATE_HUMIDITY = 50.0;
  private static final double ALTERNATE_WIND_SPEED = 2.0;


  public static SensorData getSensorData(String sensorId, LocalDate date,
                                                 Double temperature, Double humidity,
                                                 Double windSpeed) {
    return new SensorData(sensorId, date, temperature, humidity, windSpeed);
  }

  public static SensorData getSensorData() {
    return getSensorData(SENSOR_ID, DATE, TEMPERATURE, HUMIDITY,
        WIND_SPEED);
  }

  public static SensorData getSensorData(String sensorId, double temperature) {
    return getSensorData(sensorId, DATE, temperature, HUMIDITY,
        WIND_SPEED);
  }


  public static SensorData getSensorData(String sensorId) {
    return getSensorData(sensorId, DATE, TEMPERATURE, HUMIDITY,
        WIND_SPEED);
  }

  public static SensorData getAlternateSensorData(String sensorId) {
    return getSensorData(sensorId, ALTERNATE_DATE,
        ALTERNATE_TEMPERATURE, ALTERNATE_HUMIDITY, ALTERNATE_WIND_SPEED);
  }
}
