package com.demo.weather_service.service.mapper;

import static com.demo.weather_service.util.StatisticUtil.getAverage;
import static com.demo.weather_service.util.StatisticUtil.getMax;
import static com.demo.weather_service.util.StatisticUtil.getMin;
import static com.demo.weather_service.util.StatisticUtil.getSum;

import com.demo.weather_service.data.SensorData;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StatisticsMapper {

  Logger logger = LoggerFactory.getLogger(StatisticsMapper.class);

  public Map<String, Double> mapStatistics(String statistic, List<SensorData> sensorData,
                                           List<String> metrics) {

    Map<String, Double> mappedStatistics;

    switch (statistic.toLowerCase(Locale.ROOT)) {
      case "average" -> mappedStatistics = mapAverageStatistic(sensorData, metrics);
      case "sum" -> mappedStatistics = mapSumStatistic(sensorData, metrics);
      case "max" -> mappedStatistics = mapMaxStatistic(sensorData, metrics);
      case "min" -> mappedStatistics = mapMinStatistic(sensorData, metrics);
      default -> {
        logger.warn("msg={}, requested_statistic={}", "Invalid statistic", statistic);
        mappedStatistics = Map.of("invalid-statistic: " + statistic, 0.0);
      }
    }

    return mappedStatistics;
  }

  public Map<String, Double> mapOverallStatistics(String statistic,
                                                  Set<Map.Entry<String, List<SensorData>>> entries,
                                                  List<String> metrics) {

    List<SensorData> sensorDataList =
        entries.stream().flatMap(entry -> entry.getValue().stream()).toList();

    return mapStatistics(statistic, sensorDataList, metrics);
  }

  public Map<String, Double> mapAverageStatistic(List<SensorData> sensorDatas,
                                                 List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();

    for (String metric : metricsToProcess) {
      switch (metric.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metric, getAverage(sensorDatas.size(),
            sensorDatas.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metric, getAverage(sensorDatas.size(),
            sensorDatas.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metric, getAverage(sensorDatas.size(),
            sensorDatas.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> {
          logger.warn("msg={}, invalid_metric={}",
              "Unable to perform average operation on invalid metric, mapping invalid-metric",
              metric);
          statisticsMap.put("invalid-metric", 0.0);
        }

      }
    }
    return statisticsMap;
  }

  public Map<String, Double> mapSumStatistic(List<SensorData> sensorData,
                                             List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();
    for (String metric : metricsToProcess) {
      switch (metric.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metric,
            getSum(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metric,
            getSum(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metric,
            getSum(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> {
          logger.warn("msg={}, invalid_metric={}",
              "Unable to perform max operation on invalid metric, mapping invalid-metric", metric);
          statisticsMap.put("invalid-metric", 0.0);
        }
      }
    }
    return statisticsMap;
  }

  public Map<String, Double> mapMaxStatistic(List<SensorData> sensorData,
                                             List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();
    for (String metric : metricsToProcess) {
      switch (metric.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metric,
            getMax(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metric,
            getMax(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metric,
            getMax(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> {
          logger.warn("msg={}, invalid_metric={}",
              "Unable to perform max operation on invalid metric, mapping invalid-metric", metric);
          statisticsMap.put("invalid-metric", 0.0);
        }
      }
    }
    return statisticsMap;
  }

  public Map<String, Double> mapMinStatistic(List<SensorData> sensorData,
                                             List<String> metricsToProcess) {
    Map<String, Double> metric = new HashMap<>();
    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> metric.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> metric.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> metric.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> {
          logger.warn("msg={}, invalid_metric={}",
              "Unable to perform min operation on invalid metric, mapping invalid-metric", metric);
          metric.put("invalid-metric", 0.0);
        }
      }
    }
    return metric;
  }


}
