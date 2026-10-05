package com.example.hrms.hrms.login.attendance.repository;

import com.example.hrms.hrms.login.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserIdAndAttendanceDate(
            long userId,
            LocalDate attendanceDate
    );

    List<Attendance> findByUserIdAndAttendanceDateBetween(
            long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}
