package com.example.hrms.hrms.user_create.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CreateUserRequest {
  @NotBlank
  @Size(max = 120)
  private String name;

  @NotBlank
  @Size(max = 80)
  private String username;

  @NotBlank
  @Size(min = 8, max = 100)
  private String password;

  @NotBlank
  @Email
  @Size(max = 254)
  private String email;

  @NotNull
  @DecimalMin("0.01")
  @Digits(integer = 10, fraction = 2)
  private BigDecimal monthlySalary;

  @NotBlank
  @Size(max = 120)
  private String bankName;

  @NotBlank
  @Size(max = 80)
  private String bankAccountNumber;

  @NotBlank
  @Pattern(regexp = "[A-Za-z0-9]{4,20}")
  private String bankIfsc;

  @Size(max = 20)
  private List<@NotBlank @Size(max = 1000) String> documentReferences;

  public String getName() {
    return name;
  }

  public void setName(String x) {
    name = x;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String x) {
    username = x;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String x) {
    password = x;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String x) {
    email = x;
  }

  public BigDecimal getMonthlySalary() {
    return monthlySalary;
  }

  public void setMonthlySalary(BigDecimal x) {
    monthlySalary = x;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String x) {
    bankName = x;
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(String x) {
    bankAccountNumber = x;
  }

  public String getBankIfsc() {
    return bankIfsc;
  }

  public void setBankIfsc(String x) {
    bankIfsc = x;
  }

  public List<String> getDocumentReferences() {
    return documentReferences;
  }

  public void setDocumentReferences(List<String> x) {
    documentReferences = x;
  }
}
