package com.demo.weather_service.response;

import com.demo.weather_service.data.SensorData;
import java.util.Map;
import lombok.Data;

@Data
public class SensorDatasResponse implements ApiResponse {

  private Map<String, SensorData> result;

  public SensorDatasResponse(Map<String, SensorData> result) {
    this.result = result;
  }
}
