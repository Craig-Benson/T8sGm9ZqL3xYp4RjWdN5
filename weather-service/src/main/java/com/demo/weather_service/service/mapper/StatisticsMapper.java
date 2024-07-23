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
import org.springframework.stereotype.Component;

@Component
public class StatisticsMapper {


  public Map<String, Double> mapStatistics(String statistic, List<SensorData> sensorData,
                                           List<String> metrics) {

    Map<String, Double> metricsMap;

    switch (statistic.toLowerCase(Locale.ROOT)) {
      case "average" -> metricsMap = mapAverageStatistic(sensorData, metrics);
      case "sum" -> metricsMap = mapSumStatistic(sensorData, metrics);
      case "max" -> metricsMap = mapMaxStatistic(sensorData, metrics);
      case "min" -> metricsMap = mapMinStatistic(sensorData, metrics);
      default -> metricsMap = Map.of("invalid-statistic: " + statistic, 0.0);
    }

    return metricsMap;
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

    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metricToProcess, getAverage(sensorDatas.size(),
            sensorDatas.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metricToProcess, getAverage(sensorDatas.size(),
            sensorDatas.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metricToProcess, getAverage(sensorDatas.size(),
            sensorDatas.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> statisticsMap.put("invalid-metric", 0.0);

      }
    }
    return statisticsMap;
  }

  public Map<String, Double> mapSumStatistic(List<SensorData> sensorData,
                                             List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();
    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metricToProcess,
            getSum(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metricToProcess,
            getSum(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metricToProcess,
            getSum(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> statisticsMap.put("invalid-metric", 0.0);
      }
    }
    return statisticsMap;
  }

  public Map<String, Double> mapMaxStatistic(List<SensorData> sensorData,
                                             List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();
    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metricToProcess,
            getMax(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metricToProcess,
            getMax(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metricToProcess,
            getMax(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> statisticsMap.put("invalid-metric", 0.0);
      }
    }
    return statisticsMap;
  }

  public Map<String, Double> mapMinStatistic(List<SensorData> sensorData,
                                             List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();
    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> statisticsMap.put("invalid-metric", 0.0);
      }
    }
    return statisticsMap;
  }


}
