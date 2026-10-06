package com.example.hrms.hrms.payment.entity;

import com.example.hrms.hrms.salary.entity.Salary;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "salary_payments",
    uniqueConstraints = @UniqueConstraint(name = "uk_payment_payroll", columnNames = "payroll_id"))
public class SalaryPayment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "payroll_id", nullable = false)
  private Salary payroll;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false, length = 20)
  private String status;

  @Column(nullable = false, length = 120)
  private String bankName;

  @Column(nullable = false, length = 80)
  private String maskedAccount;

  @Column(nullable = false, length = 20)
  private String bankIfsc;

  @Column(length = 120)
  private String transactionReference;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column private LocalDateTime completedAt;

  protected SalaryPayment() {}

  public SalaryPayment(Salary payroll, String bankName, String maskedAccount, String ifsc) {
    this.payroll = payroll;
    this.amount = payroll.getPayableSalary();
    this.bankName = bankName;
    this.maskedAccount = maskedAccount;
    this.bankIfsc = ifsc;
    this.status = "PENDING";
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Salary getPayroll() {
    return payroll;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getStatus() {
    return status;
  }

  public String getBankName() {
    return bankName;
  }

  public String getMaskedAccount() {
    return maskedAccount;
  }

  public String getBankIfsc() {
    return bankIfsc;
  }

  public String getTransactionReference() {
    return transactionReference;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getCompletedAt() {
    return completedAt;
  }

  public void complete(String reference) {
    this.status = "PAID";
    this.transactionReference = reference;
    this.completedAt = LocalDateTime.now();
  }
}
