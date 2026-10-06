package com.example.hrms.hrms.user_create;

import com.example.hrms.hrms.user_create.dto.CreateUserRequest;
import com.example.hrms.hrms.user_create.dto.EmployeeResponse;
import com.example.hrms.hrms.user_create.service.HrmsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class HrmsController {
  private final HrmsService service;

  public HrmsController(HrmsService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EmployeeResponse create(@Valid @RequestBody CreateUserRequest request) {
    return service.createUser(request);
  }

  @PostMapping("/{employeeId}/confirm-onboarding")
  public EmployeeResponse confirm(@PathVariable long employeeId) {
    return service.confirmOnboarding(employeeId);
  }
}
