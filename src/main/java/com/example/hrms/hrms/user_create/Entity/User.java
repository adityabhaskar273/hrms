package com.example.hrms.hrms.user_create.Entity;

import com.example.hrms.hrms.shift.entity.ShiftType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(
    name = "employees",
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_employee_email", columnNames = "email"),
      @UniqueConstraint(name = "uk_employee_username", columnNames = "username")
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String name;

  @Column(nullable = false, length = 80)
  private String username;

  @Column(nullable = false, length = 255)
  private String password;

  @Column(nullable = false, length = 254)
  private String email;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal monthlySalary;

  @Column(nullable = false, length = 30)
  private String onboardingStatus = "PENDING";

  @Column(length = 120)
  private String bankName;

  @Column(length = 80)
  private String bankAccountNumber;

  @Column(length = 20)
  private String bankIfsc;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "employee_documents", joinColumns = @JoinColumn(name = "employee_id"))
  @Column(name = "document_reference", nullable = false, length = 1000)
  private java.util.List<String> documentReferences = new java.util.ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(name = "shift_type", length = 20)
  private ShiftType shiftType = ShiftType.OFFICE;

  @Column(name = "custom_shift_start_time")
  private LocalTime customShiftStartTime;

  @Column(name = "custom_shift_end_time")
  private LocalTime customShiftEndTime;

  protected User() {}

  public User(
      String name, String username, String password, String email, BigDecimal monthlySalary) {
    this.name = name;
    this.username = username;
    this.password = password;
    this.email = email;
    this.monthlySalary = monthlySalary;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public BigDecimal getMonthlySalary() {
    return monthlySalary;
  }

  public String getOnboardingStatus() {
    return onboardingStatus;
  }

  public String getBankName() {
    return bankName;
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public String getBankIfsc() {
    return bankIfsc;
  }

  public java.util.List<String> getDocumentReferences() {
    return documentReferences;
  }

  public ShiftType getShiftType() {
    return shiftType;
  }

  public LocalTime getCustomShiftStartTime() {
    return customShiftStartTime;
  }

  public LocalTime getCustomShiftEndTime() {
    return customShiftEndTime;
  }

  public void confirmOnboarding() {
    this.onboardingStatus = "ACTIVE";
  }

  public void setPasswordHash(String encoded) {
    this.password = encoded;
  }

  public boolean isActive() {
    return "ACTIVE".equals(onboardingStatus);
  }

  public void setBankDetails(String bankName, String accountNumber, String ifsc) {
    this.bankName = bankName;
    this.bankAccountNumber = accountNumber;
    this.bankIfsc = ifsc;
  }

  public void setDocumentReferences(java.util.List<String> references) {
    this.documentReferences =
        references == null ? new java.util.ArrayList<>() : new java.util.ArrayList<>(references);
  }

  public void setShift(ShiftType type, LocalTime start, LocalTime end) {
    this.shiftType = type;
    this.customShiftStartTime = start;
    this.customShiftEndTime = end;
  }
}
