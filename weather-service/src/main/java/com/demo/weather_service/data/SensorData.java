package com.demo.weather_service.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class SensorData {

  @Id
  @GeneratedValue
  private Long id;

  @NotBlank(message = "SensorId is required")
  private String sensorId;
  @NotNull(message = "Date is required")
  private LocalDate date;
  private double temperature;
  private double humidity;
  private double windSpeed;


  protected SensorData() {
  }

  public SensorData(String sensorId, LocalDate date, double temperature, double humidity,
                    double windSpeed) {
    this.sensorId = sensorId;
    this.date = date;
    this.temperature = temperature;
    this.humidity = humidity;
    this.windSpeed = windSpeed;
  }
}
