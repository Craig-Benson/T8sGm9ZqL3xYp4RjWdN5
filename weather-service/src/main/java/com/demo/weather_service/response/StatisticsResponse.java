package com.demo.weather_service.response;

import java.util.Map;
import lombok.Data;

@Data
public class StatisticsResponse implements ApiResponse {

  private Map<String, Map<String, Double>> result;

  public StatisticsResponse(Map<String, Map<String, Double>> result)  {
    this.result = result;
  }

}
