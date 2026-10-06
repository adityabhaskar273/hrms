package com.example.hrms.hrms.login.attendance.repository;

import com.example.hrms.hrms.login.attendance.entity.Attendance;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
  @Query(
      "select a from Attendance a where a.user.id=:employeeId and a.logoutTime is null order by"
          + " a.loginTime desc")
  List<Attendance> findOpenSessions(@Param("employeeId") long employeeId);

  @Query(
      "select a from Attendance a where a.user.id=:employeeId and a.loginTime < :end and"
          + " (a.logoutTime is null or a.logoutTime > :start)")
  List<Attendance> findOverlappingSessions(
      @Param("employeeId") long employeeId,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);
}
