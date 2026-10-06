package com.example.hrms.hrms.login.attendance.controller;

import com.example.hrms.hrms.login.attendance.dto.*;
import com.example.hrms.hrms.login.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
  private final AttendanceService service;

  public AttendanceController(AttendanceService s) {
    service = s;
  }

  @PostMapping("/punch")
  public ResponseEntity<AttendanceResponse> punch(@Valid @RequestBody AttendanceRequest r) {
    return ResponseEntity.ok(service.punch(r));
  }
}
