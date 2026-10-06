package com.example.hrms.hrms.login.attendance.dto;

import java.time.LocalDateTime;

public record AttendanceResponse(
    long employeeId,
    String action,
    LocalDateTime eventTime,
    Long sessionId,
    Long workedMinutes,
    String message) {}
