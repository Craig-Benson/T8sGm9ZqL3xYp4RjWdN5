package com.demo.weather_service.controllers.user;

import com.demo.weather_service.response.ApiResponse;
import com.demo.weather_service.response.SensorDataResponse;
import com.demo.weather_service.service.ResponseService;
import com.demo.weather_service.service.SensorDataService;
import com.demo.weather_service.util.DateRangeValidatorUtil;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final SensorDataService sensorDataService;
  private final ResponseService responseService;

  public UserController(SensorDataService sensorDataService, ResponseService responseService) {
    this.sensorDataService = sensorDataService;
    this.responseService = responseService;
  }

  @GetMapping("/weather-service")
  public ResponseEntity<SensorDataResponse> getLatestState() {
    return responseService.createLatestStateResponse(sensorDataService.getLatestState());
  }

  @GetMapping("/weather-service/{sensorIds}")
  public ResponseEntity<ApiResponse> getLatestStateForSensorIds(
      @PathVariable List<String> sensorIds) {

    return responseService.createLatestStatesResponse(
        sensorDataService.getLatestStateForSensorIds(sensorIds));
  }

  @GetMapping("/weather-service/{sensorIds}/{metrics}/{statistic}")
  public ResponseEntity<ApiResponse> getMetricsStatistics(
      @PathVariable List<String> sensorIds,
      @PathVariable List<String> metrics,
      @PathVariable String statistic,
      @RequestParam(value = "from", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate from,
      @RequestParam(value = "to", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate to) {

    if (!DateRangeValidatorUtil.withinValidDateRange(from, to)) {
      return responseService.createInvalidDateResponse();
    }

    return responseService.createStatisticsResponse(
        sensorDataService.getMetricsStatisticsForSensors(sensorIds, statistic, metrics, from,
            to));

  }

  @GetMapping("/weather-service/all-sensors/{metrics}/{statistic}")
  public ResponseEntity<ApiResponse> getAllSensorIdMetricsStatistics(
      @PathVariable List<String> metrics,
      @PathVariable String statistic,
      @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate from,
      @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      LocalDate to) {
    if (!DateRangeValidatorUtil.withinValidDateRange(from, to)) {
      return responseService.createInvalidDateResponse();
    }
    return responseService.createStatisticsResponse(
        sensorDataService.getMetricsStatisticsForAllSensors(metrics, statistic, from,
            to));
  }
}
