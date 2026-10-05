package com.example.hrms.hrms.salary.entity;


import com.example.hrms.hrms.user_create.Entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "salaries",
                         uniqueConstraints = {
                              @UniqueConstraint(
                                      columnNames = {"user_id", "salary_month", "salary_year"}
                              )
                         })
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "salary_month", nullable = false)
    private int salaryMonth;

    @Column(name = "salary_year", nullable = false)
    private int salaryYear;

    @Column(nullable = false)
    private int totalWorkingDays;

    @Column(nullable = false)
    private int payableDays;

    @Column(nullable = false)
    private int unpaidDays;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlySalary;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal payableSalary;

    public Salary(User user, int salaryMonth, int salaryYear,
                  int totalWorkingDays, int payableDays, int unpaidDays,
                  BigDecimal monthlySalary, BigDecimal payableSalary){
        this.user = user;
        this.salaryMonth = salaryMonth;
        this.salaryYear = salaryYear;
        this.totalWorkingDays = totalWorkingDays;
        this.payableDays = payableDays;
        this.unpaidDays = unpaidDays;
        this.monthlySalary = monthlySalary;
        this.payableSalary = payableSalary;
    }

    public void updateSalary(int totalWorkingDays, int payableDays, int unpaidDays,
                             BigDecimal monthlySalary, BigDecimal payableSalary){
        this.totalWorkingDays = totalWorkingDays;
        this.payableDays = payableDays;
        this.unpaidDays = unpaidDays;
        this.monthlySalary = monthlySalary;
        this.payableSalary = payableSalary;
    }


}
