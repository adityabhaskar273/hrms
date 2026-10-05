package com.example.hrms.hrms.salary.service;

import com.example.hrms.hrms.salary.dto.SalaryCalculationRequest;
import com.example.hrms.hrms.salary.dto.SalaryResponse;

public interface SalaryService {

    SalaryResponse calculateSalary(
            SalaryCalculationRequest salaryCalculationRequest
    );

    SalaryResponse getSalary(
            long userId, int month, int year
    );
}
