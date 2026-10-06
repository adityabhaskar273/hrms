package com.example.hrms.hrms.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PaymentCompletionRequest {
  @NotBlank
  @Size(max = 120)
  private String transactionReference;

  public String getTransactionReference() {
    return transactionReference;
  }

  public void setTransactionReference(String x) {
    transactionReference = x;
  }
}
