package com.example.hrms.hrms.payment.dto;

import com.example.hrms.hrms.payment.entity.SalaryPayment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    long paymentId,
    long employeeId,
    int month,
    int year,
    BigDecimal amount,
    String status,
    String bankName,
    String maskedAccount,
    String bankIfsc,
    String transactionReference,
    LocalDateTime createdAt,
    LocalDateTime completedAt) {
  public static PaymentResponse from(SalaryPayment p) {
    return new PaymentResponse(
        p.getId(),
        p.getPayroll().getUser().getId(),
        p.getPayroll().getSalaryMonth(),
        p.getPayroll().getSalaryYear(),
        p.getAmount(),
        p.getStatus(),
        p.getBankName(),
        p.getMaskedAccount(),
        p.getBankIfsc(),
        p.getTransactionReference(),
        p.getCreatedAt(),
        p.getCompletedAt());
  }
}
