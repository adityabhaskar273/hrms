package com.example.hrms.hrms.salary.service;

import com.example.hrms.hrms.salary.dto.*;

public interface SalaryService {
  PayrollResponse calculateSalary(SalaryCalculationRequest request);

  SalaryResponse getSalary(long employeeId, int month, int year);
}
