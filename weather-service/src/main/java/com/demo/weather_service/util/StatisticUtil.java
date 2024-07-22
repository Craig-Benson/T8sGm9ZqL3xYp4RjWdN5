package com.demo.weather_service.util;

import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StatisticUtil {

  public static double getAverage(int count, DoubleStream metricStream) {
    return metricStream.sum() / count;
  }

  public static double getSum(DoubleStream metricStream) {
    return metricStream.sum();
  }

  public static double getMin(DoubleStream metricStream) {
    OptionalDouble min = metricStream.min();
    if (min.isPresent()) {
      return min.getAsDouble();
    }
    //TODO: fix
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
  }

  public static double getMax(DoubleStream metricStream) {
    OptionalDouble max = metricStream.max();

    if (max.isPresent()) {
      return max.getAsDouble();
    }
    //TODO: fix
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
  }
}
