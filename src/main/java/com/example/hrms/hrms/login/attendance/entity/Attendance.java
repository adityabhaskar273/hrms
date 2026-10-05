package com.example.hrms.hrms.login.attendance.entity;


import com.example.hrms.hrms.user_create.Entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance",
        uniqueConstraints = {
              @UniqueConstraint(
                      columnNames ={"user_id", "attendance_date"}
              )
        }
        )
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private AttendanceStatus status;

    public Attendance(User user, LocalDate attendanceDate, LocalDateTime loginTime, AttendanceStatus status){
        this.user = user;
        this.attendanceDate= attendanceDate;
        this.loginTime= loginTime;
        this.status= status;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public LocalDate getAttendanceDate(){
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate){
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus(){
        return status;
    }

    public void setStatus(AttendanceStatus status){
        this.status = status;
    }

    public Attendance() {
    }

    public LocalDateTime getLoginTime(){
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime){
        this.loginTime = loginTime;
    }

    public LocalDateTime getLogoutTime(){
        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }

}
