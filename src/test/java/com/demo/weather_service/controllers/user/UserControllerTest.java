package com.demo.weather_service.controllers.user;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.weather_service.data.SensorData;
import com.demo.weather_service.fixtures.SensorDataFixture;
import com.demo.weather_service.repository.SensorDataRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SensorDataRepository repository;

  @BeforeEach
  public void setup() {
    repository.deleteAll();
    repository.flush();
  }

  @Test
  public void shouldRetrieveLatestState() throws Exception {
    SensorData testSensorData = SensorDataFixture.getDbSensorData(1L);
    repository.save(testSensorData);

    String expected =
        "{\"result\":{\"id\":1,\"sensorId\":\"sensor-1\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}}";
    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service"))
        .andExpect(status().isOk())
        .andExpect(content().string(expected))
        .andReturn();

  }

  @Test
  public void shouldRetrieveLatestStateForAllRequestedSensors() throws Exception {
    SensorData testFirstSensorData = SensorDataFixture.getDbSensorData(1L, "sensor-1");
    SensorData testSecondSensorData = SensorDataFixture.getDbSensorData(2L, "sensor-2");
    SensorData testThirdSensorData = SensorDataFixture.getDbSensorData(3L, "sensor-3");

    repository.save(testFirstSensorData);
    repository.save(testSecondSensorData);
    repository.save(testThirdSensorData);

    String expectedSensorDataJson =
        "{\"result\":{\"sensor-1\":{\"id\":1,\"sensorId\":\"sensor-1\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}," +
            "\"sensor-2\":{\"id\":2,\"sensorId\":\"sensor-2\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}," +
            "\"sensor-3\":{\"id\":3,\"sensorId\":\"sensor-3\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}}}";


    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service/sensor-1,sensor-2,sensor-3"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }

  @Test
  public void shouldReturnNotFoundResponseWhenNoSensorDataFound() throws Exception {
    SensorData testFirstSensorData = SensorDataFixture.getDbSensorData(1L, "sensor-1");
    SensorData testSecondSensorData = SensorDataFixture.getDbSensorData(2L, "sensor-2");
    SensorData testThirdSensorData = SensorDataFixture.getDbSensorData(3L, "sensor-3");

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
    SensorData testFirstSensorData = SensorDataFixture.getDbSensorData(1L, "sensor-1");
    SensorData testSecondSensorData = SensorDataFixture.getDbSensorData(2L, "sensor-2");
    SensorData testThirdSensorData = SensorDataFixture.getDbSensorData(3L, "sensor-3");

    repository.save(testFirstSensorData);
    repository.save(testSecondSensorData);
    repository.save(testThirdSensorData);

    String expectedSensorDataJson =
        "{\"result\":{\"sensor-1\":{\"id\":1,\"sensorId\":\"sensor-1\",\"date\":\"2024-01-01\",\"temperature\":10.0,\"humidity\":60.0,\"windSpeed\":5.0}}}";


    mockMvc.perform(MockMvcRequestBuilders.get("/weather-service/sensor-1,invalid-sensor"))
        .andExpect(status().is(200))
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }


  @Test
  public void shouldProcessesSingleSensorIdWithSingleMetricWithSingleStatistic() throws Exception {


    SensorData testSensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 1), 10.0, 60.0, 5.0);
    SensorData testAlternateFirstSensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 2), 5.0, 50.0, 2.0);

    repository.save(testSensorData);
    repository.save(testAlternateFirstSensorData);

    String expectedSensorDataJson =
        "{\"result\":{\"overall\":{\"Humidity\":55.0},\"sensor-1\":{\"Humidity\":55.0}}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1/Humidity/Average?from=2024-01-01&to=2024-01-02"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();

  }

  @Test
  public void shouldProcessesMultipleSensorIdLatestStateWithSingleStatisticWhenNoDatePresent()
      throws Exception {
    SensorData testSensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 1), 10.0, 60.0, 5.0);
    SensorData testAlternateFirstSensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 1), 5.0, 40.0, 2.0);
    SensorData testSecondSensorData =
        SensorDataFixture.getSensorData("sensor-2", LocalDate.of(2024, 1, 1), 5.0, 50.0, 3.0);

    repository.save(testSensorData);
    repository.save(testAlternateFirstSensorData);
    repository.save(testSecondSensorData);

    String expectedSensorDataJson = "{\"result\":{\"overall\":{\"Humidity\":55.0}," +
        "\"sensor-1\":{\"Humidity\":60.0},\"sensor-2\":{\"Humidity\":50.0}}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1,sensor-2/Humidity/Average"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();

  }

  @Test
  public void shouldProcessesMultipleSensorIdLatestStateWithMultipleStatisticWhenNoDatePresent()
      throws Exception {
    SensorData testSensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 1), 10.0, 60.0, 5.0);
    SensorData testAlternateFirstSensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 1), 5.0, 40.0, 2.0);
    SensorData testSecondSensorData =
        SensorDataFixture.getSensorData("sensor-2", LocalDate.of(2024, 1, 1), 5.0, 50.0, 3.0);

    repository.save(testSensorData);
    repository.save(testAlternateFirstSensorData);
    repository.save(testSecondSensorData);

    String expectedSensorDataJson =
        "{\"result\":{\"overall\":{\"temperature\":7.5,\"humidity\":55.0,\"windspeed\":4.0}," +
            "\"sensor-1\":{\"temperature\":10.0,\"humidity\":60.0,\"windspeed\":5.0}," +
            "\"sensor-2\":{\"temperature\":5.0,\"humidity\":50.0,\"windspeed\":3.0}}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1,sensor-2/humidity,temperature,windspeed/Average"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();

  }

  @Test
  public void shouldProcessesMultipleSensorIdWithMultipleMetricWithMultipleStatisticWithValidDatePresent()
      throws Exception {
    SensorData sensorOneData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 2), 10.0, 60.0, 5.0);
    SensorData sensorOneAlternateData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 2), 5.0, 50.0,
            2.0);

    SensorData sensorTwoData =
        SensorDataFixture.getSensorData("sensor-2", LocalDate.of(2024, 1, 2), 5.0, 50.0,
            2.5);
    SensorData sensorTwoAlternateData =
        SensorDataFixture.getSensorData("sensor-2", LocalDate.of(2024, 1, 2), 2.5, 40.0,
            5.0);

    repository.save(sensorOneData);
    repository.save(sensorOneAlternateData);
    repository.save(sensorTwoData);
    repository.save(sensorTwoAlternateData);

    String expectedSensorDataJson =
        "{\"result\":{\"overall\":{\"temperature\":5.625,\"humidity\":50.0,\"windspeed\":3.625}," +
            "\"sensor-1\":{\"temperature\":7.5,\"humidity\":55.0,\"windspeed\":3.5}," +
            "\"sensor-2\":{\"temperature\":3.75,\"humidity\":45.0,\"windspeed\":3.75}}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1,sensor-2/humidity,temperature,windspeed/average?from=2024-01-01&to=2024-01-03"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }

  @Test
  public void shouldProcessesAllSensorIdWithMultipleMetricWithMultipleStatisticWithValidDatePresent()
      throws Exception {
    SensorData sensorOneData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 1), 10.0, 60.0, 5.0);
    SensorData sensorOneAlternateData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.of(2024, 1, 2), 5.0, 50.0,
            2.0);

    SensorData sensorTwoData =
        SensorDataFixture.getSensorData("sensor-2", LocalDate.of(2024, 1, 1), 5.0, 50.0,
            2.5);
    SensorData sensorTwoAlternateData =
        SensorDataFixture.getSensorData("sensor-2", LocalDate.of(2024, 1, 2), 2.5, 40.0,
            5.0);

    SensorData sensorThreeData =
        SensorDataFixture.getSensorData("sensor-3", LocalDate.of(2024, 1, 1), 2.5, 40.0,
            1.0);
    SensorData sensorThreeAlternateData =
        SensorDataFixture.getSensorData("sensor-3", LocalDate.of(2024, 1, 2), 1.0, 30.0,
            2.5);

    repository.save(sensorOneData);
    repository.save(sensorOneAlternateData);
    repository.save(sensorTwoData);
    repository.save(sensorTwoAlternateData);
    repository.save(sensorThreeData);
    repository.save(sensorThreeAlternateData);

    String expectedSensorDataJson =
        "{\"result\":{\"overall\":{\"temperature\":4.333333333333333,\"humidity\":45.0,\"windspeed\":3.0}," +
            "\"sensor-1\":{\"temperature\":7.5,\"humidity\":55.0,\"windspeed\":3.5}," +
            "\"sensor-2\":{\"temperature\":3.75,\"humidity\":45.0,\"windspeed\":3.75}," +
            "\"sensor-3\":{\"temperature\":1.75,\"humidity\":35.0,\"windspeed\":1.75}}}";


    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/all-sensors/humidity,temperature,windspeed/average?from=2024-01-01&to=2024-01-02"))
        .andExpect(status().isOk())
        .andExpect(content().string(expectedSensorDataJson))
        .andReturn();
  }

  @Test
  void shouldReturnInvalidResponseWhenRequestDateIsGreaterThan30DaysForMultipleSensors()
      throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/sensor-1,sensor-2/humidity,temperature,windspeed/average?from=2024-01-01&to=2024-02-02"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("{\"result\":\"Invalid date range\"}"))
        .andReturn();
  }

  @Test
  void shouldReturnInvalidResponseWhenRequestDateIsGreaterThan30DaysForAllSensors()
      throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.get(
            "/weather-service/all-sensors/humidity,temperature,windspeed/average?from=2024-01-01&to=2024-02-02"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("{\"result\":\"Invalid date range\"}"))
        .andReturn();
  }
}


