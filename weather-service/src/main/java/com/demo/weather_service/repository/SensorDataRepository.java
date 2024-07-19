package com.demo.weather_service.repository;

import com.demo.weather_service.data.SensorData;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
  SensorData findTopByOrderByDateDesc();

  SensorData findFirstBySensorIdOrderByDateDesc(String sensorId);

  List<SensorData> findSensorDataBySensorIdAndDateBetween(String sensorId, LocalDate startDate,
                                                          LocalDate endDate);

  @Query("select distinct u.sensorId from SensorData u")
  List<String> getAllSensorIds();

}
