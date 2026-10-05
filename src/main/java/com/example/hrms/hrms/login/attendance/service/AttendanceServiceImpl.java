package com.example.hrms.hrms.login.attendance.service;

import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.login.attendance.dto.AttendanceRequest;
import com.example.hrms.hrms.login.attendance.entity.Attendance;
import com.example.hrms.hrms.login.attendance.entity.AttendanceStatus;
import com.example.hrms.hrms.login.attendance.repository.AttendanceRepository;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, UserRepository userRepository){
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }


    @Override
    public String punchIn(AttendanceRequest attendanceRequest){
        if(attendanceRequest == null){
            throw new RuntimeException("Attendance Request cannot be empty");
        }

        Optional<User> user = userRepository.findById(attendanceRequest.getUserId());

        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime loginTime = LocalDateTime.now();

        Optional<Attendance> existingAttendance = attendanceRepository.findByUserIdAndAttendanceDate(
                attendanceRequest.getUserId(),
                today
        );

        if(existingAttendance.isPresent()){
           return "Attendance already marked for this day";
        }

        Attendance attendance = new Attendance(
                user.get(),
                today,
                loginTime,
                AttendanceStatus.PRESENT
        );

        attendanceRepository.save(attendance);

        return "Attendance marked Successfully";

    }


    @Override
    public String punchOut(AttendanceRequest attendanceRequest){
        if(attendanceRequest == null){
            throw new RuntimeException("Attendance Request cannot be empty");
        }

        LocalDate today = LocalDate.now();

        Optional<Attendance> attendance = attendanceRepository.findByUserIdAndAttendanceDate(
                attendanceRequest.getUserId(),
                today);

        if(attendance.isEmpty()){
            return "Login attendance not found for today";
        }

        Attendance attendanceRecord = attendance.get();

        if(attendanceRecord.getLogoutTime() != null){
            return "Logout already marked for today";
        }

        LocalDateTime logoutTime = LocalDateTime.now();

        attendanceRecord.setLogoutTime(logoutTime);

        Duration duration = Duration.between(
                attendanceRecord.getLoginTime(),
                logoutTime);

        long totalMinutes = duration.toMinutes();

        if(totalMinutes>=480){
            attendanceRecord.setStatus(AttendanceStatus.PRESENT);
        }else{
            attendanceRecord.setStatus(AttendanceStatus.ABSENT);
        }
        attendanceRepository.save(attendanceRecord);

        return "Logout Successful, Total Working TIme:"
                + totalMinutes / 60
                + "hours"
                + totalMinutes % 60
                + "minutes";

    }
}

