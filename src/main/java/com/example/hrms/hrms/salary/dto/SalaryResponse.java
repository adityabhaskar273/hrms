package com.example.hrms.hrms.salary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class SalaryResponse {

    private long userId;
    private int month;
    private int year;
    private int totalWorkingDays;
    private int payableDays;
    private int unpaidDays;
    private BigDecimal monthlySalary;
    private BigDecimal payableSalary;


}
