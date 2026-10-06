package com.example.hrms.hrms.salary.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SalaryResponse(
    long employeeId,
    String employeeName,
    int month,
    int year,
    int totalWorkingDays,
    int fullPayDays,
    int halfPayDays,
    int unpaidDays,
    int overtimeAchievementDays,
    BigDecimal overtimeHours,
    BigDecimal monthlySalary,
    BigDecimal payableSalary,
    LocalDateTime calculatedAt) {}
