package com.example.hrms.hrms.login.attendance.entity;

import com.example.hrms.hrms.user_create.Entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "attendance_sessions",
    indexes = {@Index(name = "ix_attendance_employee_start", columnList = "employee_id, punch_in")})
public class Attendance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "employee_id", nullable = false)
  private User user;

  @Column(name = "punch_in", nullable = false)
  private LocalDateTime loginTime;

  @Column(name = "punch_out")
  private LocalDateTime logoutTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private AttendanceStatus status = AttendanceStatus.PRESENT;

  protected Attendance() {}

  public Attendance(User user, LocalDateTime at) {
    this.user = user;
    this.loginTime = at;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public LocalDateTime getLoginTime() {
    return loginTime;
  }

  public LocalDateTime getLogoutTime() {
    return logoutTime;
  }

  public AttendanceStatus getStatus() {
    return status;
  }

  public void punchOut(LocalDateTime at) {
    if (logoutTime != null) throw new IllegalStateException("Session is already closed");
    if (!at.isAfter(loginTime))
      throw new IllegalArgumentException("Punch-out must be after punch-in");
    logoutTime = at;
  }
}
