package com.example.hrms.hrms.payment.dto;

import jakarta.validation.constraints.*;

public class DisbursementRequest {
  @Positive private long employeeId;

  @Min(1)
  @Max(12)
  private int month;

  @Positive private int year;

  public long getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(long x) {
    employeeId = x;
  }

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
