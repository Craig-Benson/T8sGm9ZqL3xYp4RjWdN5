package com.demo.weather_service.util;


import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;

public class DateRangeValidatorUtil {

  public static boolean withinValidDateRange(LocalDate from, LocalDate to) {

    if (from != null && to != null) {
      return DAYS.between(from, to) <= 30;
    }
    return true;
  }
}
