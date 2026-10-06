package com.example.hrms.hrms.login.attendance.service;

import com.example.hrms.hrms.login.attendance.dto.AttendanceRequest;
import com.example.hrms.hrms.login.attendance.dto.AttendanceResponse;

public interface AttendanceService {
  AttendanceResponse punch(AttendanceRequest request);
}
