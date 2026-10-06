package com.example.hrms.hrms.user_create.dto;

import java.math.BigDecimal;
import java.util.List;

public record EmployeeResponse(
    Long id,
    String name,
    String username,
    String email,
    BigDecimal monthlySalary,
    String onboardingStatus,
    String bankName,
    String maskedBankAccount,
    String bankIfsc,
    List<String> documentReferences) {
  public static EmployeeResponse from(com.example.hrms.hrms.user_create.Entity.User user) {
    String account = user.getBankAccountNumber();
    String masked =
        account == null
            ? null
            : "*".repeat(Math.max(0, account.length() - 4))
                + account.substring(Math.max(0, account.length() - 4));
    return new EmployeeResponse(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        user.getMonthlySalary(),
        user.getOnboardingStatus(),
        user.getBankName(),
        masked,
        user.getBankIfsc(),
        List.copyOf(user.getDocumentReferences()));
  }
}
