package com.example.hrms.hrms.payment.service;

import com.example.hrms.hrms.payment.dto.*;
import com.example.hrms.hrms.payment.entity.SalaryPayment;
import com.example.hrms.hrms.payment.repository.SalaryPaymentRepository;
import com.example.hrms.hrms.salary.entity.Salary;
import com.example.hrms.hrms.salary.repository.SalaryRepository;
import com.example.hrms.hrms.user_create.Entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentServiceImpl implements PaymentService {
  private final SalaryRepository payroll;
  private final SalaryPaymentRepository payments;

  public PaymentServiceImpl(SalaryRepository p, SalaryPaymentRepository s) {
    payroll = p;
    payments = s;
  }

  @Override
  @Transactional
  public PaymentResponse disburse(DisbursementRequest r) {
    Salary salary =
        payroll
            .findWithLockByUserIdAndSalaryMonthAndSalaryYear(
                r.getEmployeeId(), r.getMonth(), r.getYear())
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Calculated payroll not found"));
    if (salary.getPayableSalary().signum() <= 0)
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "There is no payable salary for this employee and period");
    if (salary.getUser().getBankAccountNumber() == null || salary.getUser().getBankIfsc() == null)
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "Employee bank details are incomplete");
    if (payments.findByPayrollId(salary.getId()).isPresent())
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "A payment request already exists for this payroll");
    User employee = salary.getUser();
    String account = employee.getBankAccountNumber();
    String masked =
        "*".repeat(Math.max(0, account.length() - 4))
            + account.substring(Math.max(0, account.length() - 4));
    salary.markPaymentRequestCreated();
    payroll.save(salary);
    return PaymentResponse.from(
        payments.save(
            new SalaryPayment(salary, employee.getBankName(), masked, employee.getBankIfsc())));
  }

  @Override
  @Transactional
  public PaymentResponse complete(long id, PaymentCompletionRequest r) {
    SalaryPayment p =
        payments
            .findById(id)
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment request not found"));
    if (!"PENDING".equals(p.getStatus()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment request is not pending");
    p.complete(r.getTransactionReference().trim());
    return PaymentResponse.from(payments.save(p));
  }
}
