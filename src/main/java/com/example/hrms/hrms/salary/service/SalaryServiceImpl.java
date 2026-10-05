package com.example.hrms.hrms.salary.service;


import com.example.hrms.hrms.login.attendance.entity.Attendance;
import com.example.hrms.hrms.login.attendance.entity.AttendanceStatus;
import com.example.hrms.hrms.login.attendance.repository.AttendanceRepository;
import com.example.hrms.hrms.salary.dto.SalaryCalculationRequest;
import com.example.hrms.hrms.salary.dto.SalaryResponse;
import com.example.hrms.hrms.salary.entity.Salary;
import com.example.hrms.hrms.salary.repository.SalaryRepository;
import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService{


    private final SalaryRepository salaryRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public SalaryResponse calculateSalary(SalaryCalculationRequest salaryCalculationRequest){
        User user = userRepository.findById(salaryCalculationRequest.getUserId()).orElseThrow(()-> new RuntimeException("user not found"));

        YearMonth yearMonth = YearMonth.of(salaryCalculationRequest.getYear(),
                salaryCalculationRequest.getMonth());

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        int totalWorkingDays = countWorkingDays(startDate, endDate);

        List<Attendance> attendanceList = attendanceRepository.findByUserIdAndAttendanceDateBetween(
                user.getId(),
                startDate,
                endDate);

        Set<LocalDate> paidDates = new HashSet<>();

        for(Attendance attendance: attendanceList) {
            boolean isWeekDay = attendance.getAttendanceDate().getDayOfWeek() != DayOfWeek.SATURDAY
                    && attendance.getAttendanceDate().getDayOfWeek() != DayOfWeek.SUNDAY;

            boolean isPaidLeave = attendance.getStatus() == AttendanceStatus.PAID_LEAVE;

            boolean isCompletedPresentDay = attendance.getStatus() == AttendanceStatus.PRESENT
                    && attendance.getLogoutTime() != null;

            if (isWeekDay && (isPaidLeave || isCompletedPresentDay)) {
                paidDates.add(attendance.getAttendanceDate());

            }
        }

                int payableDays = paidDates.size();
                int unpaidDays = totalWorkingDays - payableDays;

                BigDecimal dailySalary = user.getMonthlySalary().divide(
                        BigDecimal.valueOf(totalWorkingDays),
                        2,
                        RoundingMode.HALF_UP
                );


                BigDecimal payableSalary = dailySalary.multiply(BigDecimal.valueOf(payableDays))
                        .setScale(2, RoundingMode.HALF_UP);

                Salary salary = salaryRepository.
                        findByUserIdAndSalaryMonthAndSalaryYear(
                                user.getId(),
                                salaryCalculationRequest.getMonth(),
                                salaryCalculationRequest.getYear()
                        ).orElseGet(()->new Salary(
                                user,
                                salaryCalculationRequest.getMonth(),
                                salaryCalculationRequest.getYear(),
                                totalWorkingDays,
                                payableDays,
                                unpaidDays,
                                user.getMonthlySalary(),
                                payableSalary
                        ));

                salary.updateSalary(
                        totalWorkingDays,
                        payableDays,
                        unpaidDays,
                        user.getMonthlySalary(),
                        payableSalary
                );

                Salary savedSalary = salaryRepository.save(salary);

                return new SalaryResponse(
                        savedSalary.getUser().getId(),
                        savedSalary.getSalaryMonth(),
                        savedSalary.getSalaryYear(),
                        savedSalary.getTotalWorkingDays(),
                        savedSalary.getPayableDays(),
                        savedSalary.getUnpaidDays(),
                        savedSalary.getMonthlySalary(),
                        savedSalary.getPayableSalary()
                );
    }


    @Override
    public SalaryResponse getSalary(long userId, int month, int year){
        Salary salary = salaryRepository.findByUserIdAndSalaryMonthAndSalaryYear(userId, month, year)
                .orElseThrow(()-> new RuntimeException("Salary Record not found"));

        return new SalaryResponse(
                salary.getUser().getId(),
                salary.getSalaryMonth(),
                salary.getSalaryYear(),
                salary.getTotalWorkingDays(),
                salary.getPayableDays(),
                salary.getUnpaidDays(),
                salary.getMonthlySalary(),
                salary.getPayableSalary()
        );

    }

    private int countWorkingDays(LocalDate startDate, LocalDate endDate){
        int totalWorkingDays = 0;
        LocalDate date = startDate;


        while (!date.isAfter(endDate)){
            if(date.getDayOfWeek() != DayOfWeek.SATURDAY &&
            date.getDayOfWeek() != DayOfWeek.SUNDAY){
                totalWorkingDays++;
            }
            date = date.plusDays(1);

        }
        return totalWorkingDays;
    }


}
