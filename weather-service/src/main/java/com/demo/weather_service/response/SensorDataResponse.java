package com.demo.weather_service.response;

import com.demo.weather_service.data.SensorData;
import lombok.Data;

@Data
public class SensorDataResponse implements ApiResponse {
  private SensorData result;

  public SensorDataResponse(SensorData result) {
    this.result = result;
  }
}
