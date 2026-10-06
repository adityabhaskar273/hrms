package com.example.hrms.hrms.payment.repository;

import com.example.hrms.hrms.payment.entity.SalaryPayment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryPaymentRepository extends JpaRepository<SalaryPayment, Long> {
  Optional<SalaryPayment> findByPayrollId(long payrollId);
}
