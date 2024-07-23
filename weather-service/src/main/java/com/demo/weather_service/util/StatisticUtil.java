package com.demo.weather_service.util;

import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticUtil {
  static Logger logger = LoggerFactory.getLogger(StatisticUtil.class);

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
    logger.error("msg= {}, default: {}", "Min was not able to be processed returning default.", 0.0);
    return 0.0;
  }

  public static double getMax(DoubleStream metricStream) {

    OptionalDouble max = metricStream.max();

    if (max.isPresent()) {
      return max.getAsDouble();
    }
    logger.error("msg= {}, default: {}", "Max was not able to be processed returning default.", 0.0);
    return 0.0;
  }
}
