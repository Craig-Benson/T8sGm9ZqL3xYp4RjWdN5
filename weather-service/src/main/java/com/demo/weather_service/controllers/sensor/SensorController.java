package com.demo.weather_service.controllers.sensor;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.repository.SensorDataRepository;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class SensorController {

  private final SensorDataRepository repository;

  public SensorController(SensorDataRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/weather-service/update")
  public ResponseEntity<SensorData> save(@RequestBody @Validated SensorData sensorData)
      throws MalformedURLException, URISyntaxException {
    SensorData savedData = repository.save(sensorData);

    URI location =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .build(savedData.getId())
            .toURL().toURI();

    return ResponseEntity.created(location).body(savedData);
  }
}
