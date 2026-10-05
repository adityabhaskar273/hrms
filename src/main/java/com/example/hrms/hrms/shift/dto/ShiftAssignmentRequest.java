package com.example.hrms.hrms.shift.dto;


import com.example.hrms.hrms.shift.entity.ShiftType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ShiftAssignmentRequest {

    @Positive(message = "User id must be valid")
    private long userId;

    @NotNull(message = "Shift Type is Required")
    private ShiftType shiftType;

    private LocalTime customShiftStartTime;

    private LocalTime customShiftEndTime;
}
