package com.example.hrms.hrms.login.attendance.service;

import com.example.hrms.hrms.login.attendance.dto.*;
import com.example.hrms.hrms.login.attendance.entity.Attendance;
import com.example.hrms.hrms.login.attendance.repository.AttendanceRepository;
import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AttendanceServiceImpl implements AttendanceService {
  private final AttendanceRepository attendance;
  private final UserRepository users;

  public AttendanceServiceImpl(AttendanceRepository a, UserRepository u) {
    attendance = a;
    users = u;
  }

  @Override
  @Transactional
  public AttendanceResponse punch(AttendanceRequest r) {
    User user =
        users
            .findByIdForUpdate(r.getUserId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    if (!user.isActive())
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "Employee onboarding is not confirmed");
    var open = attendance.findOpenSessions(user.getId());
    if (open.size() > 1)
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "Multiple open attendance sessions require correction");
    LocalDateTime now = LocalDateTime.now();
    if (open.isEmpty()) {
      Attendance saved = attendance.save(new Attendance(user, now));
      return new AttendanceResponse(
          user.getId(), "PUNCH_IN", now, saved.getId(), null, "Punch-in recorded");
    }
    Attendance session = open.getFirst();
    if (Duration.between(session.getLoginTime(), now).compareTo(Duration.ofHours(20)) > 0)
      throw new ResponseStatusException(
          HttpStatus.CONFLICT,
          "Open attendance session is older than 20 hours; contact HR to correct it");
    session.punchOut(now);
    attendance.save(session);
    return new AttendanceResponse(
        user.getId(),
        "PUNCH_OUT",
        now,
        session.getId(),
        Duration.between(session.getLoginTime(), now).toMinutes(),
        "Punch-out recorded");
  }
}
