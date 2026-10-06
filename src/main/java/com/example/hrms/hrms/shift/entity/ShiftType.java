package com.example.hrms.hrms.shift.entity;

import java.time.LocalTime;

public enum ShiftType {
  OFFICE(LocalTime.of(9, 0), LocalTime.of(18, 0)),
  FACTORY(LocalTime.of(9, 0), LocalTime.of(18, 0)),
  FIELD(LocalTime.of(9, 0), LocalTime.of(18, 0)),
  CUSTOM(null, null);

  private final LocalTime defaultStartTime;
  private final LocalTime defaultEndTime;

  ShiftType(LocalTime defaultStartTime, LocalTime defaultEndTime) {
    this.defaultStartTime = defaultStartTime;
    this.defaultEndTime = defaultEndTime;
  }

  public LocalTime getDefaultStartTime() {
    return defaultStartTime;
  }

  public LocalTime getDefaultEndTime() {
    return defaultEndTime;
  }

  public boolean isCustomShift() {
    return this == CUSTOM;
  }
}
