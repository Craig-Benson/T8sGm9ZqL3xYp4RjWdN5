package com.demo.weather_service.service;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.repository.SensorDataRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SensorDataService {

  private final SensorDataRepository repository;

  public SensorDataService(SensorDataRepository repository) {
    this.repository = repository;
  }


  public double getMetricAverageForSensor(String sensorId, String metric,
                                          LocalDate from,
                                          LocalDate to) {

    List<SensorData> sensorDatas =
        repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to);

    return switch (metric.toLowerCase(Locale.ROOT)) {
      case "temperature" -> getAverage(sensorDatas.size(),
          sensorDatas.stream().mapToDouble(SensorData::getTemperature));
      case "humidity" -> getAverage(sensorDatas.size(),
          sensorDatas.stream().mapToDouble(SensorData::getHumidity));
      case "windspeed" -> getAverage(sensorDatas.size(),
          sensorDatas.stream().mapToDouble(SensorData::getWindSpeed));
      default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
    };

  }

  public double getMetricSumForSensor(String sensorId, String metric, LocalDate from,
                                      LocalDate to) {
    List<SensorData> sensorDatas =
        repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to);

    return switch (metric.toLowerCase(Locale.ROOT)) {
      case "temperature" -> getSum(sensorDatas.stream().mapToDouble(SensorData::getTemperature));
      case "humidity" -> getSum(sensorDatas.stream().mapToDouble(SensorData::getHumidity));
      case "windspeed" -> getSum(sensorDatas.stream().mapToDouble(SensorData::getWindSpeed));
      default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
    };
  }

  public double getMetricMinForSensor(String sensorId, String metric, LocalDate from,
                                      LocalDate to) {

    List<SensorData> sensorDatas =
        repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to);
    return switch (metric.toLowerCase(Locale.ROOT)) {
      case "temperature" -> getMin(sensorDatas.stream().mapToDouble(SensorData::getTemperature));
      case "humidity" -> getMin(sensorDatas.stream().mapToDouble(SensorData::getHumidity));
      case "windspeed" -> getMin(sensorDatas.stream().mapToDouble(SensorData::getWindSpeed));
      default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
    };
  }

  public double getMetricMaxForSensor(String sensorId, String metric, LocalDate from,
                                      LocalDate to) {

    List<SensorData> sensorDatas =
        repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to);

    if (!sensorDatas.isEmpty()) {
      return switch (metric.toLowerCase(Locale.ROOT)) {
        case "temperature" -> getMax(sensorDatas.stream().mapToDouble(SensorData::getTemperature));
        case "humidity" -> getMax(sensorDatas.stream().mapToDouble(SensorData::getHumidity));
        case "windspeed" -> getMax(sensorDatas.stream().mapToDouble(SensorData::getWindSpeed));
        default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Request");
      };
    }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
  }

  public Map<String, Map<String, Double>> getMetricsStatisticsForSensors(List<String> sensorIds,
                                                                         String statistic,
                                                                         List<String> metrics,
                                                                         LocalDate from,
                                                                         LocalDate to
  ) {

    if (from == null) {
      from = LocalDate.now().minusDays(1);
    }
    if (to == null) {
      to = LocalDate.now();
    }

    List<List<SensorData>> sensorDatas = retrievePersistedSensorData(sensorIds, from, to);
    Map<String, Map<String, Double>> statistics = new HashMap<>();

    //TODO
    //stream
    //empty check
    //get
    sensorDatas.forEach(sensorDataList -> statistics.put(
        sensorDataList.get(0).getSensorId(),
        processMetrics(sensorDataList.size(), statistic, sensorDataList, metrics)));

    return statistics;
  }

  public Map<String, Map<String, Double>> getMetricsStatisticsForAllSensors(List<String> metrics,
                                                                            String statistic,
                                                                            LocalDate from,
                                                                            LocalDate to
  ) {
    return getMetricsStatisticsForSensors(repository.getAllSensorIds(), statistic, metrics, from,
        to);
  }

  private List<List<SensorData>> retrievePersistedSensorData(List<String> sensorIds,
                                                             LocalDate from,
                                                             LocalDate to) {
    List<List<SensorData>> sensorDatas = new ArrayList<>();
    for (String sensorId : sensorIds) {
      sensorDatas.add(repository.findSensorDataBySensorIdAndDateBetween(sensorId, from, to));
    }


    return sensorDatas;
  }

  private Map<String, Double> processMetrics(int count,
                                             String operation,
                                             List<SensorData> sensorData,
                                             List<String> metricsToProcess) {

    Map<String, Double> metricsMap = new HashMap<>();
    switch (operation.toLowerCase(Locale.ROOT)) {
      case "average" -> mapAverageMetrics(count, sensorData, metricsToProcess, metricsMap);
      case "sum" -> mapSumMetrics(sensorData, metricsToProcess, metricsMap);
      case "max" -> mapMaxMetrics(sensorData, metricsToProcess, metricsMap);
      case "min" -> mapMinMetrics(sensorData, metricsToProcess, metricsMap);
      default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
    }

    return metricsMap;
  }

  private void mapAverageMetrics(int count, List<SensorData> sensorDatas,
                                 List<String> metricsToProcess,
                                 Map<String, Double> metricsMap) {
    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> metricsMap.put(metricToProcess,
            getAverage(count, sensorDatas.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> metricsMap.put(metricToProcess,
            getAverage(count, sensorDatas.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> metricsMap.put(metricToProcess,
            getAverage(count, sensorDatas.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
      }
    }


  }

  private void mapSumMetrics(List<SensorData> sensorData,
                             List<String> metricsToProcess,
                             Map<String, Double> metricsMap) {

    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> metricsMap.put(metricToProcess,
            getSum(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> metricsMap.put(metricToProcess,
            getSum(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> metricsMap.put(metricToProcess,
            getSum(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
      }
    }

  }

  private void mapMaxMetrics(List<SensorData> sensorData,
                             List<String> metricsToProcess,
                             Map<String, Double> metricsMap) {
    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> metricsMap.put(metricToProcess,
            getMax(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> metricsMap.put(metricToProcess,
            getMax(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> metricsMap.put(metricToProcess,
            getMax(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
      }
    }

  }

  private void mapMinMetrics(List<SensorData> sensorData,
                             List<String> metricsToProcess,
                             Map<String, Double> metricsMap) {

    for (String metricToProcess : metricsToProcess) {
      switch (metricToProcess.toLowerCase(Locale.ROOT)) {
        case "temperature" -> metricsMap.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getTemperature)));
        case "humidity" -> metricsMap.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getHumidity)));
        case "windspeed" -> metricsMap.put(metricToProcess,
            getMin(sensorData.stream().mapToDouble(SensorData::getWindSpeed)));
        default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
      }
    }
  }


  private double getAverage(int count, DoubleStream metricStream) {
    return metricStream.sum() / count;
  }

  private double getSum(DoubleStream metricStream) {
    return metricStream.sum();
  }

  private double getMin(DoubleStream metricStream) {
    OptionalDouble min = metricStream.min();
    if (min.isPresent()) {
      return min.getAsDouble();
    }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
  }

  private double getMax(DoubleStream metricStream) {
    OptionalDouble max = metricStream.max();

    if (max.isPresent()) {
      return max.getAsDouble();
    }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data found");
  }
}





