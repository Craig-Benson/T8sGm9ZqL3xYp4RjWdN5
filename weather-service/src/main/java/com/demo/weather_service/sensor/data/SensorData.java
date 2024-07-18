package com.demo.weather_service.sensor.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class SensorData {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank(message = "SensorId is required")
  private String sensorId;
  @NotNull(message = "Date is required")
  private LocalDateTime timestamp;
  private double temperature;
  private double humidity;
  private double windSpeed;

  protected SensorData() {
  }

  public SensorData(String sensorId, LocalDateTime timestamp, double temperature, double humidity,
                    double windSpeed) {
    this.sensorId = sensorId;
    this.timestamp = timestamp;
    this.temperature = temperature;
    this.humidity = humidity;
    this.windSpeed = windSpeed;
  }

  public String getSensorId() {
    return sensorId;
  }

  public void setSensorId(String sensorId) {
    this.sensorId = sensorId;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public double getHumidity() {
    return humidity;
  }

  public void setHumidity(Double humidity) {
    this.humidity = humidity;
  }

  public double getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(Double windSpeed) {
    this.windSpeed = windSpeed;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
