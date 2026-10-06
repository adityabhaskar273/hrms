package com.example.hrms.hrms.payment.service;

import com.example.hrms.hrms.payment.dto.*;

public interface PaymentService {
  PaymentResponse disburse(DisbursementRequest request);

  PaymentResponse complete(long paymentId, PaymentCompletionRequest request);
}
