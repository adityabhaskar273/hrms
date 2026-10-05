package com.example.hrms.hrms.salary.controller;


import com.example.hrms.hrms.salary.dto.SalaryCalculationRequest;
import com.example.hrms.hrms.salary.dto.SalaryResponse;
import com.example.hrms.hrms.salary.entity.Salary;
import com.example.hrms.hrms.salary.repository.SalaryRepository;
import com.example.hrms.hrms.salary.service.SalaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salary")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @PostMapping("/calculate")
    public ResponseEntity<SalaryResponse> calculateSalary(
            @Valid @RequestBody SalaryCalculationRequest salaryCalculationRequest){
        SalaryResponse response = salaryService.calculateSalary(salaryCalculationRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SalaryResponse> getSalary(
            @PathVariable long userId,
            @RequestParam int month,
            @RequestParam int year){

        SalaryResponse response = salaryService.getSalary(userId, month, year);

        return ResponseEntity.ok(response);
    }
}
