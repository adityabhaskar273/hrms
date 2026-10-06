package com.example.hrms.hrms.salary.repository;

import com.example.hrms.hrms.salary.entity.Salary;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
  Optional<Salary> findByUserIdAndSalaryMonthAndSalaryYear(long userId, int month, int year);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Salary> findWithLockByUserIdAndSalaryMonthAndSalaryYear(
      long userId, int month, int year);

  List<Salary> findAllBySalaryMonthAndSalaryYearOrderByUserId(int month, int year);
}
