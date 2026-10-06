package com.example.hrms.hrms.shift.controller;

import com.example.hrms.hrms.shift.dto.ShiftAssignmentRequest;
import com.example.hrms.hrms.shift.service.ShiftService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
  private final ShiftService shiftService;

  public ShiftController(ShiftService shiftService) {
    this.shiftService = shiftService;
  }

  @PostMapping("/assign")
  public ResponseEntity<String> assignShift(@Valid @RequestBody ShiftAssignmentRequest request) {
    return ResponseEntity.ok(shiftService.assignShift(request));
  }
}
