package com.example.hrms.hrms.salary.entity;

import com.example.hrms.hrms.user_create.Entity.User;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "payroll_records",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_payroll_employee_period",
            columnNames = {"employee_id", "salary_year", "salary_month"}))
public class Salary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "employee_id", nullable = false)
  private User user;

  @Column(name = "salary_month", nullable = false)
  private int salaryMonth;

  @Column(name = "salary_year", nullable = false)
  private int salaryYear;

  @Column(nullable = false)
  private int totalWorkingDays;

  @Column(nullable = false)
  private int fullPayDays;

  @Column(nullable = false)
  private int halfPayDays;

  @Column(nullable = false)
  private int unpaidDays;

  @Column(nullable = false)
  private int overtimeAchievementDays;

  @Column(nullable = false, precision = 8, scale = 2)
  private BigDecimal overtimeHours;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal monthlySalary;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal payableSalary;

  @Column(nullable = false)
  private LocalDateTime calculatedAt;

  @Column(nullable = false)
  private boolean paymentRequestCreated;

  protected Salary() {}

  public Salary(User user, int month, int year) {
    this.user = user;
    salaryMonth = month;
    salaryYear = year;
  }

  public void recalculate(
      int workdays,
      int fullDays,
      int halfDays,
      int unpaid,
      int overtimeDays,
      BigDecimal overtime,
      BigDecimal base,
      BigDecimal payable) {
    if (paymentRequestCreated)
      throw new IllegalStateException("Payroll is locked after a payment request is created");
    totalWorkingDays = workdays;
    fullPayDays = fullDays;
    halfPayDays = halfDays;
    unpaidDays = unpaid;
    overtimeAchievementDays = overtimeDays;
    overtimeHours = overtime;
    monthlySalary = base;
    payableSalary = payable;
    calculatedAt = LocalDateTime.now();
  }

  public void markPaymentRequestCreated() {
    this.paymentRequestCreated = true;
  }

  public boolean isPaymentRequestCreated() {
    return paymentRequestCreated;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public int getSalaryMonth() {
    return salaryMonth;
  }

  public int getSalaryYear() {
    return salaryYear;
  }

  public int getTotalWorkingDays() {
    return totalWorkingDays;
  }

  public int getFullPayDays() {
    return fullPayDays;
  }

  public int getHalfPayDays() {
    return halfPayDays;
  }

  public int getUnpaidDays() {
    return unpaidDays;
  }

  public int getOvertimeAchievementDays() {
    return overtimeAchievementDays;
  }

  public BigDecimal getOvertimeHours() {
    return overtimeHours;
  }

  public BigDecimal getMonthlySalary() {
    return monthlySalary;
  }

  public BigDecimal getPayableSalary() {
    return payableSalary;
  }

  public LocalDateTime getCalculatedAt() {
    return calculatedAt;
  }
}
