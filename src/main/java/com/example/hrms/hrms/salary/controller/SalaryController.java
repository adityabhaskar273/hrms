package com.example.hrms.hrms.salary.controller;

import com.example.hrms.hrms.salary.dto.*;
import com.example.hrms.hrms.salary.service.SalaryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
public class SalaryController {
  private final SalaryService service;

  public SalaryController(SalaryService s) {
    service = s;
  }

  @PostMapping("/calculate")
  public ResponseEntity<PayrollResponse> calculate(@Valid @RequestBody SalaryCalculationRequest r) {
    return ResponseEntity.ok(service.calculateSalary(r));
  }

  @GetMapping("/employees/{employeeId}")
  public ResponseEntity<SalaryResponse> get(
      @PathVariable long employeeId, @RequestParam int month, @RequestParam int year) {
    return ResponseEntity.ok(service.getSalary(employeeId, month, year));
  }
}
