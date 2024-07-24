package com.demo.weather_service.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
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
  @NotNull(message = "Temperature is required")
  private Double temperature;
  @Min(value = 0, message = "Humidity must be greater than or equal to 0")
  @NotNull(message = "Humidity is required")
  private Double humidity;
  @Min(value = 0, message = "Wind Speed must be greater than or equal to 0")
  @NotNull(message = "Wind Speed is required")
  private Double windSpeed;

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
