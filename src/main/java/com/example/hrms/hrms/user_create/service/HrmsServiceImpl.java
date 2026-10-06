package com.example.hrms.hrms.user_create.service;

import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.dto.CreateUserRequest;
import com.example.hrms.hrms.user_create.dto.EmployeeResponse;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HrmsServiceImpl implements HrmsService {
  private final UserRepository users;
  private final PasswordEncoder passwordEncoder;

  public HrmsServiceImpl(UserRepository users, PasswordEncoder passwordEncoder) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public EmployeeResponse createUser(CreateUserRequest r) {
    if (users.existsByEmailIgnoreCase(r.getEmail()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
    if (users.existsByUsernameIgnoreCase(r.getUsername()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
    User user =
        new User(
            r.getName().trim(),
            r.getUsername().trim(),
            r.getPassword(),
            r.getEmail().trim().toLowerCase(),
            r.getMonthlySalary());
    user.setPasswordHash(passwordEncoder.encode(r.getPassword()));
    user.setBankDetails(
        r.getBankName().trim(),
        r.getBankAccountNumber().trim(),
        r.getBankIfsc().trim().toUpperCase());
    user.setDocumentReferences(r.getDocumentReferences());
    return EmployeeResponse.from(users.save(user));
  }

  @Override
  @Transactional
  public EmployeeResponse confirmOnboarding(long id) {
    User user =
        users
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    if (user.isActive())
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Onboarding is already confirmed");
    user.confirmOnboarding();
    return EmployeeResponse.from(users.save(user));
  }
}
