package com.example.hrms.hrms.shift.dto;

import com.example.hrms.hrms.shift.entity.ShiftType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;

public class ShiftAssignmentRequest {
  @Positive(message = "Employee id must be positive")
  private long userId;

  @NotNull(message = "Shift type is required")
  private ShiftType shiftType;

  private LocalTime customShiftStartTime;
  private LocalTime customShiftEndTime;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long value) {
    userId = value;
  }

  public ShiftType getShiftType() {
    return shiftType;
  }

  public void setShiftType(ShiftType value) {
    shiftType = value;
  }

  public LocalTime getCustomShiftStartTime() {
    return customShiftStartTime;
  }

  public void setCustomShiftStartTime(LocalTime value) {
    customShiftStartTime = value;
  }

  public LocalTime getCustomShiftEndTime() {
    return customShiftEndTime;
  }

  public void setCustomShiftEndTime(LocalTime value) {
    customShiftEndTime = value;
  }
}
