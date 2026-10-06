package com.example.hrms.hrms.login.attendance.dto;

import jakarta.validation.constraints.Positive;

public class AttendanceRequest {
  @Positive private long userId;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long id) {
    userId = id;
  }
}
