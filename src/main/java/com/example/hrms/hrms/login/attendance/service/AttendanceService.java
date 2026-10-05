package com.example.hrms.hrms.login.attendance.service;

import com.example.hrms.hrms.login.attendance.dto.AttendanceRequest;

public interface AttendanceService {

    String punchIn(AttendanceRequest attendanceRequest);

    String punchOut(AttendanceRequest attendanceRequest);
}
