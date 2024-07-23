package com.demo.weather_service.service;

import static com.demo.weather_service.util.StatisticUtil.getAverage;
import static com.demo.weather_service.util.StatisticUtil.getMax;
import static com.demo.weather_service.util.StatisticUtil.getMin;
import static com.demo.weather_service.util.StatisticUtil.getSum;

import com.demo.weather_service.data.SensorData;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class StatisticsMapper {

  Map<String, Double> mapAverageStatistic(int count, List<SensorData> sensorDatas,
                                          List<String> metricsToProcess) {
    Map<String, Double> statisticsMap = new HashMap<>();

    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> statisticsMap.put(metricToProcess, getAverage(count,
            sensorDatas.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> statisticsMap.put(metricToProcess, getAverage(count,
            sensorDatas.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> statisticsMap.put(metricToProcess, getAverage(count,
            sensorDatas.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> statisticsMap.put("invalid-metric", 0.0);

      }
    }
    return statisticsMap;
  }

  Map<String, Double> mapSumStatistic(List<SensorData> sensorData,
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

  Map<String, Double> mapMaxStatistic(List<SensorData> sensorData,
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

  Map<String, Double> mapMinStatistic(List<SensorData> sensorData,
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
