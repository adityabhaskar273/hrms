package com.example.hrms.hrms.salary.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;


@Data
public class SalaryCalculationRequest {

    @Positive(message = "User ID must be Valid")
    private long userId;

    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private int month;

    @Positive(message = "Year must be valid")
    private int year;

}
