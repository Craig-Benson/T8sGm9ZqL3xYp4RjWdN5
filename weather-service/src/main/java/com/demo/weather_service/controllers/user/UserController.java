package com.demo.weather_service.controllers.user;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.repository.SensorDataRepository;
import com.demo.weather_service.service.SensorDataService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//TODO: rename?
public class UserController {

  private Logger logger = LoggerFactory.getLogger(UserController.class);

  private final SensorDataRepository repository;

  private final SensorDataService sensorDataService;

  public UserController(SensorDataRepository repository, SensorDataService sensorDataService) {
    this.repository = repository;
    this.sensorDataService = sensorDataService;
  }

  @GetMapping("/weather-service")
  public SensorData getLatestState() {
    return repository.findTopByOrderByDateDesc();
  }

  // should i keep this?
//  @GetMapping("/weather-metrics/{sensorId}")
//  public SensorData getLatestStateForSensorId(@PathVariable String sensorId) {
//    return repository.findFirstBySensorIdOrderByDateDesc(sensorId);
//  }

  @GetMapping("/weather-service/{sensorIds}")
  public List<SensorData> getLatestStateForSensorIds(@PathVariable List<String> sensorIds) {

    List<SensorData> sensorDataList = new ArrayList<>();
    for (String sensorId : sensorIds) {
      sensorDataList.add(repository.findFirstBySensorIdOrderByDateDesc(sensorId));
    }

    return sensorDataList;
  }


  @GetMapping("/weather-service/{sensorIds}/{metrics}/{statistic}")
  public Map<String, Map<String, Double>> getMetricsStatistics(@PathVariable List<String> sensorIds,
                                                               @PathVariable List<String> metrics,
                                                               @PathVariable String statistic,
                                                               @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 LocalDate from,
                                                               @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate to) {



    return sensorDataService.getMetricsStatisticsForSensors(sensorIds, statistic, metrics, from,
        to);
  }

  @GetMapping("/weather-service/all-sensors/{metrics}/{statistic}")
  public Map<String, Map<String, Double>> getAllSensorIdMetricsStatistics(
      @PathVariable List<String> metrics,
      @PathVariable String statistic,
      @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate from,
      @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate to) {


    return sensorDataService.getMetricsStatisticsForAllSensors(metrics, statistic, from,
        to);
  }

  @GetMapping("/weather-service/{sensorId}/{metric}/sum/{from}/{to}")
  public double getSumMetric(@PathVariable String sensorId,
                             @PathVariable String metric,
                             @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate from,
                             @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate to) {

    return sensorDataService.getMetricSumForSensor(sensorId, metric, from, to);
  }


  @GetMapping("/weather-service/{sensorId}/{metric}/min/{from}/{to}")
  public double getMinMetric(@PathVariable String sensorId,
                             @PathVariable String metric,
                             @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate from,
                             @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate to) {

    return sensorDataService.getMetricMinForSensor(sensorId, metric, from, to);
  }

  @GetMapping("/weather-service/{sensorId}/{metric}/max/{from}/{to}")
  public double getMaxMetric(@PathVariable String sensorId,
                             @PathVariable String metric,
                             @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate from,
                             @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                               LocalDate to) {

    return sensorDataService.getMetricMaxForSensor(sensorId, metric, from, to);
  }

}
