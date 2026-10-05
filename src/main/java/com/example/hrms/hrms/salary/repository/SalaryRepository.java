package com.example.hrms.hrms.salary.repository;

import com.example.hrms.hrms.login.attendance.entity.Attendance;
import com.example.hrms.hrms.salary.entity.Salary;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    Optional<Salary> findByUserIdAndSalaryMonthAndSalaryYear(long userId,
                                                             int salaryMonth,
                                                             int salaryYear);
}
