package com.example.hrms.hrms.payment.controller;

import com.example.hrms.hrms.payment.dto.*;
import com.example.hrms.hrms.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
  private final PaymentService service;

  public PaymentController(PaymentService s) {
    service = s;
  }

  @PostMapping("/disburse")
  public ResponseEntity<PaymentResponse> disburse(@Valid @RequestBody DisbursementRequest r) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.disburse(r));
  }

  @PostMapping("/{paymentId}/complete")
  public ResponseEntity<PaymentResponse> complete(
      @PathVariable long paymentId, @Valid @RequestBody PaymentCompletionRequest r) {
    return ResponseEntity.ok(service.complete(paymentId, r));
  }
}
