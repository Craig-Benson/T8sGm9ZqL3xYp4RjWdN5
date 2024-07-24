package com.demo.weather_service.sensor.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WithMockUser
class SensorControllerTest {

  @Autowired
  private SensorDataRepository repository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper jacksonObjectMapper;

  @BeforeEach
  public void setup() {
    repository.deleteAll();
    repository.flush();
  }

  @Test
  void shouldReturnCreatedResponseWhenValidPost() throws Exception {

    SensorData sensorData = SensorDataFixture.getSensorDataOne();

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedSensorIdIsNull() throws Exception {

    SensorData sensorData = SensorDataFixture.getSensorData(null, LocalDate.now(), 10.0, 10.0, 10.0);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("SensorId is required")))
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedDateIsNull() throws Exception {

    SensorData sensorData = SensorDataFixture.getSensorData("sensor-1", null, 10.0, 10.0, 10.0);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("Date is required")))
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedTemperatureIsNull() throws Exception {

    SensorData sensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.now(), null, 10.0, 10.0);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("Temperature is required")))
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedHumidityIsNull() throws Exception {

    SensorData sensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.now(), 10.0, null, 10.0);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("Humidity is required")))
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedWindSpeedIsNull() throws Exception {

    SensorData sensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.now(), 10.0, 10.0, null);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("Wind Speed is required")))
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedHumidityIsLessThanZero() throws Exception {

    SensorData sensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.now(), 10.0, -1.0, 10.0);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("Humidity must be greater than or equal to 0")))
        .andReturn();

  }

  @Test
  void shouldReturnBadRequestWhenProvidedWindSpeedIsLessThanZero() throws Exception {

    SensorData sensorData =
        SensorDataFixture.getSensorData("sensor-1", LocalDate.now(), 10.0, 10.0, -1.0);

    String json = jacksonObjectMapper.writeValueAsString(sensorData);

    mockMvc.perform(
            post("/weather-service/update").contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("Invalid input")))
        .andExpect(jsonPath("$.details[0]", is("Wind Speed must be greater than or equal to 0")))
        .andReturn();

  }
}