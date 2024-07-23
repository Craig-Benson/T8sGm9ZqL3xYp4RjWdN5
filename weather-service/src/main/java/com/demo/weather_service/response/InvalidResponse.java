package com.demo.weather_service.response;

import lombok.Data;

@Data
public class InvalidResponse implements ApiResponse {

  private String result;

  public InvalidResponse(String result) {
  this.result = result;
  }
}
