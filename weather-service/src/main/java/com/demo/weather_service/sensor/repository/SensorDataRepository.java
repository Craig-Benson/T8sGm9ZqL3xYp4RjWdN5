package com.demo.weather_service.sensor.repository;

import com.demo.weather_service.sensor.data.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

}
