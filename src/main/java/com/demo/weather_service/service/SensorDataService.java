package com.demo.weather_service.service;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.repository.SensorDataRepository;
import com.demo.weather_service.service.mapper.StatisticsMapper;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SensorDataService {
  Logger logger = LoggerFactory.getLogger(SensorDataService.class);

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

      SensorData sensorDataEntry = repository.findFirstBySensorIdOrderByDateDesc(sensorId);
      if (sensorDataEntry == null) {
        logger.info("msg={}, sensor_id={}", "Nothing found for sensorId", sensorId);
        continue;
      }
      sensorData.put(sensorId, sensorDataEntry);
    }

    return sensorData;
  }


  public Map<String, Map<String, Double>> getMetricsStatisticsForSensors(
      List<String> sensorIds,
      String statistic,
      List<String> metrics,
      LocalDate from,
      LocalDate to
  ) {

    Map<String, Map<String, Double>> statistics = new TreeMap<>();

    Map<String, List<SensorData>> sensorDataBySensorId =
        isDatesPresent(from, to) ? getSensorDataBetweenDates(sensorIds, from, to): getLatestSensorDataFor(sensorIds);

    statistics.put("overall",
        statisticsMapper.mapOverallStatistics(statistic, sensorDataBySensorId.entrySet(), metrics));

    sensorDataBySensorId.forEach((key, value) ->
        statistics.put(key, statisticsMapper.mapStatistics(statistic, value, metrics)));


    return statistics;
  }


  private Map<String, List<SensorData>> getLatestSensorDataFor(List<String> sensorIds) {
    Map<String, List<SensorData>> map = new HashMap<>();

    for (String sensorId : sensorIds) {
      SensorData sensorData = repository.findFirstBySensorIdOrderByDateDesc(sensorId);
      if (sensorData.getSensorId() == null) {
        logger.info("msg={}, sensor_id={}", "Nothing found for sensorId", sensorId);
        continue;
      }
      map.put(sensorId, List.of(sensorData));
    }
    return map;
  }

  private Map<String, List<SensorData>> getSensorDataBetweenDates(List<String> sensorIds,
                                                                  LocalDate from,
                                                                  LocalDate to) {
    Map<String, List<SensorData>> mappedSensorData = new HashMap<>();
    for (String sensorId : sensorIds) {
      List<SensorData> sensorData =
          repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to);
      if (sensorData.isEmpty()) {
        logger.info("msg={}, sensor_id={}", "Nothing found for sensorId", sensorId);
        continue;
      }
      mappedSensorData.put(sensorId, sensorData);

    }
    return mappedSensorData;
  }

  public Map<String, Map<String, Double>> getMetricsStatisticsForAllSensors(List<String> metrics,
                                                                            String statistic,
                                                                            LocalDate from,
                                                                            LocalDate to) {
    return getMetricsStatisticsForSensors(repository.getAllSensorIds(), statistic, metrics, from,
        to);
  }

  private boolean isDatesPresent(LocalDate from, LocalDate to) {
    if (from != null && to != null) {
      return true;
    }

    logger.info("msg={}", "No Dates present, retrieving latest");
    return false;
  }

}





