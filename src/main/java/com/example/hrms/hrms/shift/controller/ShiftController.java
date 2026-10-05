package com.example.hrms.hrms.shift.controller;


import com.example.hrms.hrms.shift.dto.ShiftAssignmentRequest;
import com.example.hrms.hrms.shift.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping("/assign")
    public ResponseEntity<String> assignShift(@Valid @RequestBody ShiftAssignmentRequest shiftAssignmentRequest){

        String response = shiftService.assignShift(shiftAssignmentRequest);
        return ResponseEntity.ok(response);

    }
}
