package com.example.hrms.hrms.user_create.service;

import com.example.hrms.hrms.user_create.dto.CreateUserRequest;
import com.example.hrms.hrms.user_create.dto.EmployeeResponse;

public interface HrmsService {
  EmployeeResponse createUser(CreateUserRequest request);

  EmployeeResponse confirmOnboarding(long employeeId);
}
