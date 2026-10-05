package com.example.hrms.hrms.login.attendance.controller;

import com.example.hrms.hrms.login.attendance.dto.AttendanceRequest;
import com.example.hrms.hrms.login.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService){
        this.attendanceService = attendanceService;
    }

    @PostMapping("/punch_in")
    public ResponseEntity<String> punchIn(@Valid  @RequestBody AttendanceRequest attendanceRequest){
        String response = attendanceService.punchIn(attendanceRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/punch_out")
    public ResponseEntity<String> punchOut(@Valid @RequestBody AttendanceRequest attendanceRequest){
        String response = attendanceService.punchOut(attendanceRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
