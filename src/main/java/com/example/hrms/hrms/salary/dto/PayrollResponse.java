package com.example.hrms.hrms.salary.dto;

import java.math.BigDecimal;
import java.util.List;

public record PayrollResponse(
    int month,
    int year,
    int employeeCount,
    int fullPayDays,
    int halfPayDays,
    int unpaidDays,
    int overtimeAchievements,
    BigDecimal totalPayable,
    List<SalaryResponse> employees) {}
