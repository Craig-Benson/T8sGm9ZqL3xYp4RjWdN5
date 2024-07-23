package com.demo.weather_service.service;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.repository.SensorDataRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SensorDataService {

  private final SensorDataRepository repository;
  private final StatisticsMapper statisticsMapper;

  public SensorDataService(SensorDataRepository repository, StatisticsMapper statisticsMapper) {
    this.repository = repository;
    this.statisticsMapper = statisticsMapper;
  }

  public SensorData getLatestState() {
    return repository.findTopByOrderByDateDesc();
  }

  public Map<String, SensorData> getLatestStateForSensorIds(List<String> sensorIds) {

    Map<String, SensorData> sensorData = new HashMap<>();
    for (String sensorId : sensorIds) {
      //queue if unavailable
      SensorData sensorDataEntry = repository.findFirstBySensorIdOrderByDateDesc(sensorId);
      if (sensorDataEntry != null) {
        sensorData.put(sensorId, sensorDataEntry);
      }
    }

    return sensorData;
  }

  //async
  public Map<String, Map<String, Double>> getMetricsStatisticsForSensors(
      List<String> sensorIds,
      String statistic,
      List<String> metrics,
      LocalDate from,
      LocalDate to
  ) {


    Map<String, Map<String, Double>> statistics = new HashMap<>();

    Map<String, List<SensorData>> sensorDataBySensorId =
        noDatesPresent(from, to) ? getLatestSensorDataFor(sensorIds) :
            getSensorDataBetweenDates(sensorIds, from, to);

    sensorDataBySensorId.forEach((key, value) ->
        statistics.put(key, processStatistics(statistic, value, metrics)));

    return statistics;
  }

  private Map<String, List<SensorData>> getLatestSensorDataFor(List<String> sensorIds) {
    Map<String, List<SensorData>> map = new HashMap<>();

    for (String sensorId : sensorIds) {
      SensorData sensorData = repository.findFirstBySensorIdOrderByDateDesc(sensorId);
      if (sensorData.getSensorId() != null) {
        map.put(sensorId, List.of(sensorData));
      }
    }
    return map;
  }

  private Map<String, List<SensorData>> getSensorDataBetweenDates(List<String> sensorIds, LocalDate from,
                                                                  LocalDate to) {
    Map<String, List<SensorData>> mappedSensorData = new HashMap<>();
    for (String sensorId : sensorIds) {
      List<SensorData> sensorData =
          repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to);
      if (!sensorData.isEmpty()) {
        mappedSensorData.put(sensorId, sensorData);
      }
    }
    return mappedSensorData;
  }

  private boolean noDatesPresent(LocalDate from, LocalDate to) {
    return from == null && to == null;
  }

  public Map<String, Map<String, Double>> getMetricsStatisticsForAllSensors(List<String> metrics,
                                                                            String statistic,
                                                                            LocalDate from,
                                                                            LocalDate to) {
    return getMetricsStatisticsForSensors(repository.getAllSensorIds(), statistic, metrics, from,
        to);
  }

  private Map<String, Double> processStatistics(String statistic,
                                                List<SensorData> sensorData,
                                                List<String> metricsToProcess) {

    Map<String, Double> metricsMap;

    switch (statistic.toLowerCase(Locale.ROOT)) {
      case "average" -> metricsMap =
          statisticsMapper.mapAverageStatistic(sensorData.size(), sensorData, metricsToProcess);
      case "sum" -> metricsMap = statisticsMapper.mapSumStatistic(sensorData, metricsToProcess);
      case "max" -> metricsMap = statisticsMapper.mapMaxStatistic(sensorData, metricsToProcess);
      case "min" -> metricsMap = statisticsMapper.mapMinStatistic(sensorData, metricsToProcess);
      default -> metricsMap = Map.of("invalid-statistic: " + statistic, 0.0);
    }


    return metricsMap;
  }

}





