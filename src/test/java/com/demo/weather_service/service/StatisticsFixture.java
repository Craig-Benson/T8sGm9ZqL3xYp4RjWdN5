package com.demo.weather_service.service;

import java.util.Map;

public class StatisticsFixture {

  public static Map<String, Double> getValidStatistics() {
    return Map.of("Humidity", 10.0, "Temperature", 10.0);
  }

  public static Map<String, Double> getValidAndInvalidStatistics() {
    return Map.of("Humidity", 10.0, "invalid-metric", 0.0);
  }
}
