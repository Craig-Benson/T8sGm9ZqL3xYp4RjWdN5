package com.demo.weather_service.controllers.sensor;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.repository.SensorDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorController {

  private final SensorDataRepository repository;
  public SensorController(SensorDataRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/weather-service/update")
  public ResponseEntity<SensorData> save(@RequestBody @Validated SensorData sensorData) {

    repository.save(sensorData);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
