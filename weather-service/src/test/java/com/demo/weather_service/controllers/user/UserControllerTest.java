package com.demo.weather_service.controllers.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.fixtures.SensorDataFixture;
import com.demo.weather_service.repository.SensorDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired

  private SensorDataRepository repository;

  @Autowired
  private ObjectMapper jacksonObjectMapper;

  @BeforeEach
  public void setup() {
    repository.deleteAll();
    repository.flush();
  }

  @Test
  public void shouldRetrieveLatestState() throws Exception {
    SensorData testSensorData = SensorDataFixture.getSensorDataForDb(1L);
    repository.save(testSensorData);

    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service"))
        .andExpect(status().isOk())
        .andExpect(content().string(jacksonObjectMapper.writeValueAsString(testSensorData)))
        .andReturn();

  }

  @Test
  public void shouldRetrieveLatestStateForAllRequestedSensors() throws Exception {
    SensorData testFirstSensorData = SensorDataFixture.getSensorDataForDb(1L, "sensor-1");
    SensorData testSecondSensorData = SensorDataFixture.getSensorDataForDb(2L, "sensor-2");
    SensorData testThirdSensorData = SensorDataFixture.getSensorDataForDb(3L, "sensor-3");

    repository.save(testFirstSensorData);
    repository.save(testSecondSensorData);
    repository.save(testThirdSensorData);

    String expectedSensorDataJson =
        "{\"sensor-1\":{\"id\":1,\"sensorId\":\"sensor-1\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}," +
            "\"sensor-2\":{\"id\":2,\"sensorId\":\"sensor-2\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}," +
            "\"sensor-3\":{\"id\":3,\"sensorId\":\"sensor-3\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}}";


    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service/sensor-1,sensor-2,sensor-3"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }

  @Test
  public void shouldReturnNotFoundResponseWhenNoSensorDataFound() throws Exception {
    SensorData testFirstSensorData = SensorDataFixture.getSensorDataForDb(1L, "sensor-1");
    SensorData testSecondSensorData = SensorDataFixture.getSensorDataForDb(2L, "sensor-2");
    SensorData testThirdSensorData = SensorDataFixture.getSensorDataForDb(3L, "sensor-3");

    repository.save(testFirstSensorData);
    repository.save(testSecondSensorData);
    repository.save(testThirdSensorData);


    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service/invalid-sensor"))
        .andExpect(status().isNotFound())
        .andReturn();
  }

  @Test
  public void shouldFilterInvalidSensorIdAndProcessValidSensorId()
      throws Exception {
    SensorData testFirstSensorData = SensorDataFixture.getSensorDataForDb(1L, "sensor-1");
    SensorData testSecondSensorData = SensorDataFixture.getSensorDataForDb(2L, "sensor-2");
    SensorData testThirdSensorData = SensorDataFixture.getSensorDataForDb(3L, "sensor-3");

    repository.save(testFirstSensorData);
    repository.save(testSecondSensorData);
    repository.save(testThirdSensorData);

    //TODO:should i fliter the null out
    String expectedSensorDataJson =
        "{\"sensor-1\":{\"id\":1,\"sensorId\":\"sensor-1\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}}";


    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service/sensor-1,invalid-sensor"))
        .andExpect(status().is(200))
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }


  @Test
  public void shouldProcessesSingleSensorIdWithSingleMetricWithSingleStatistic() throws Exception {
    SensorData testSensorData =
        SensorDataFixture.getSensorDataForDb(1L, "sensor-1", LocalDate.of(2024, 1, 1), 10, 60.0, 5);
    SensorData testAlternateFirstSensorData =
        SensorDataFixture.getSensorDataForDb(2L, "sensor-1", LocalDate.of(2024, 1, 2), 5, 50.0,
            2.0);

    repository.save(testSensorData);
    repository.save(testAlternateFirstSensorData);

    String expectedSensorDataJson = "{\"sensor-1\":{\"Humidity\":55.0}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1/Humidity/Average?from=2024-01-01&to=2024-01-02"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();

  }

//  @Test
//  public void shouldProcessesMultipleSensorIdWithSingleMetricWithSingleStatisticWhenNoDatePresent() throws Exception {
//    SensorData testSensorData =
//        SensorDataFixture.getSensorDataForDb(1L, "sensor-1", LocalDate.of(2024, 1, 1), 10, 60.0,5);
//    SensorData testAlternateFirstSensorData =
//        SensorDataFixture.getSensorDataForDb(2L, "sensor-1", LocalDate.of(2024, 1, 1), 5, 50.0,2.0);
//
//    repository.save(testSensorData);
//    repository.save(testAlternateFirstSensorData);
//
//    String expectedSensorDataJson = "{\"sensor-1\":{\"Humidity\":55.0}}";
//
//
//    mockMvc.perform(MockMvcRequestBuilders.get(
//            "/weather-service/sensor-1/Humidity/Average"))
//        .andExpect(status().isOk())
//        .andExpect(content().string(expectedSensorDataJson))
//        .andReturn();
//
//  }

  @Test
  public void shouldProcessesMultipleSensorIdWithMultipleMetricWithMultipleStatisticWithValidDatePresent()
      throws Exception {
    SensorData sensorOneData =
        SensorDataFixture.getSensorDataForDb(1L, "sensor-1", LocalDate.of(2024, 1, 1), 10, 60.0, 5);
    SensorData sensorOneAlternateData =
        SensorDataFixture.getSensorDataForDb(2L, "sensor-1", LocalDate.of(2024, 1, 2), 5, 50.0,
            2.0);

    SensorData sensorTwoData =
        SensorDataFixture.getSensorDataForDb(3L, "sensor-2", LocalDate.of(2024, 1, 1), 5, 50.0,
            2.5);
    SensorData sensorTwoAlternateData =
        SensorDataFixture.getSensorDataForDb(4L, "sensor-2", LocalDate.of(2024, 1, 2), 2.5, 40.0,
            5);

    repository.save(sensorOneData);
    repository.save(sensorOneAlternateData);
    repository.save(sensorTwoData);
    repository.save(sensorTwoAlternateData);

    String expectedSensorDataJson =
        "{\"sensor-1\":{\"temperature\":7.5,\"humidity\":55.0,\"windspeed\":3.5}," +
            "\"sensor-2\":{\"temperature\":3.75,\"humidity\":45.0,\"windspeed\":3.75}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1,sensor-2/humidity,temperature,windspeed/average?from=2024-01-01&to=2024-01-02"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }

  @Test
  public void shouldProcessesAllSensorIdWithMultipleMetricWithMultipleStatisticWithValidDatePresent()
      throws Exception {
    SensorData sensorOneData =
        SensorDataFixture.getSensorDataForDb(1L, "sensor-1", LocalDate.of(2024, 1, 1), 10, 60.0, 5);
    SensorData sensorOneAlternateData =
        SensorDataFixture.getSensorDataForDb(2L, "sensor-1", LocalDate.of(2024, 1, 2), 5, 50.0,
            2.0);

    SensorData sensorTwoData =
        SensorDataFixture.getSensorDataForDb(3L, "sensor-2", LocalDate.of(2024, 1, 1), 5, 50.0,
            2.5);
    SensorData sensorTwoAlternateData =
        SensorDataFixture.getSensorDataForDb(4L, "sensor-2", LocalDate.of(2024, 1, 2), 2.5, 40.0,
            5);

    SensorData sensorThreeData =
        SensorDataFixture.getSensorDataForDb(5L, "sensor-3", LocalDate.of(2024, 1, 1), 2.5, 40.0,
            1);
    SensorData sensorThreeAlternateData =
        SensorDataFixture.getSensorDataForDb(6L, "sensor-3", LocalDate.of(2024, 1, 2), 1, 30.0,
            2.5);

    repository.save(sensorOneData);
    repository.save(sensorOneAlternateData);
    repository.save(sensorTwoData);
    repository.save(sensorTwoAlternateData);
    repository.save(sensorThreeData);
    repository.save(sensorThreeAlternateData);

    String expectedSensorDataJson =
        "{\"sensor-1\":{\"temperature\":7.5,\"humidity\":55.0,\"windspeed\":3.5}," +
            "\"sensor-2\":{\"temperature\":3.75,\"humidity\":45.0,\"windspeed\":3.75}," +
            "\"sensor-3\":{\"temperature\":1.75,\"humidity\":35.0,\"windspeed\":1.75}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/all-sensors/humidity,temperature,windspeed/average?from=2024-01-01&to=2024-01-02"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }

}


