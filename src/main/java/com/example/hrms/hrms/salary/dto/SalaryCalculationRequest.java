package com.example.hrms.hrms.salary.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public class SalaryCalculationRequest {
  @Min(1)
  @Max(12)
  private int month;

  @Positive private int year;

  public int getMonth() {
    return month;
  }

  public void setMonth(int x) {
    month = x;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int x) {
    year = x;
  }
}
