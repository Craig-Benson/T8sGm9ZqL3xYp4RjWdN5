package com.demo.weather_service.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {

  private LocalDateTime timestamp;
  private String message;
  private List<String> details;

}
