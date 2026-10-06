package com.example.hrms.hrms;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.hrms.hrms.common.GlobalExceptionHandler;
import com.example.hrms.hrms.login.attendance.controller.AttendanceController;
import com.example.hrms.hrms.login.attendance.entity.Attendance;
import com.example.hrms.hrms.login.attendance.repository.AttendanceRepository;
import com.example.hrms.hrms.login.attendance.service.AttendanceServiceImpl;
import com.example.hrms.hrms.payment.controller.PaymentController;
import com.example.hrms.hrms.payment.entity.SalaryPayment;
import com.example.hrms.hrms.payment.repository.SalaryPaymentRepository;
import com.example.hrms.hrms.payment.service.PaymentServiceImpl;
import com.example.hrms.hrms.salary.controller.SalaryController;
import com.example.hrms.hrms.salary.entity.Salary;
import com.example.hrms.hrms.salary.repository.SalaryRepository;
import com.example.hrms.hrms.salary.service.SalaryServiceImpl;
import com.example.hrms.hrms.shift.entity.ShiftType;
import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.HrmsController;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import com.example.hrms.hrms.user_create.service.HrmsServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class HrmsFlowIntegrationTest {
  private UserRepository users;
  private AttendanceRepository attendance;
  private SalaryRepository salaries;
  private SalaryPaymentRepository payments;

  @BeforeEach
  void setUp() {
    users = mock(UserRepository.class);
    attendance = mock(AttendanceRepository.class);
    salaries = mock(SalaryRepository.class);
    payments = mock(SalaryPaymentRepository.class);
  }

  @Test
  void onboardingCreatesPendingEmployeeThenConfirmationActivatesThem() throws Exception {
    MockMvc mvc = mvc(new HrmsController(new HrmsServiceImpl(users, new BCryptPasswordEncoder())));
    AtomicReference<User> stored = new AtomicReference<>();
    when(users.existsByEmailIgnoreCase(anyString())).thenReturn(false);
    when(users.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
    when(users.save(any(User.class)))
        .thenAnswer(
            invocation -> {
              User employee = invocation.getArgument(0);
              ReflectionTestUtils.setField(employee, "id", 41L);
              stored.set(employee);
              return employee;
            });
    when(users.findById(41L)).thenAnswer(invocation -> Optional.ofNullable(stored.get()));

    mvc.perform(
            post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"Asha Rao","username":"asha","password":"StrongPassword1","email":"asha@example.com","monthlySalary":2200.00,
                     "bankName":"Example Bank","bankAccountNumber":"123456789012","bankIfsc":"EXAM0001234","documentReferences":["private-docs/asha/id.pdf"]}
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.onboardingStatus").value("PENDING"))
        .andExpect(jsonPath("$.maskedBankAccount").value("********9012"))
        .andExpect(jsonPath("$.password").doesNotExist());
    assert stored.get() != null;
    assert stored.get().getPassword().startsWith("$2a$");
    assert !stored.get().isActive();

    mvc.perform(post("/api/employees/41/confirm-onboarding"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.onboardingStatus").value("ACTIVE"));
    assert stored.get().isActive();
  }

  @Test
  void oneAttendanceEndpointAlternatesBetweenPunchInAndPunchOut() throws Exception {
    MockMvc mvc = mvc(new AttendanceController(new AttendanceServiceImpl(attendance, users)));
    User employee = employee(7L, "ACTIVE", "2000.00");
    when(users.findByIdForUpdate(7L)).thenReturn(Optional.of(employee));
    AtomicReference<Attendance> open = new AtomicReference<>();
    when(attendance.findOpenSessions(7L))
        .thenAnswer(
            invocation -> {
              Attendance session = open.get();
              return session == null || session.getLogoutTime() != null
                  ? List.of()
                  : List.of(session);
            });
    when(attendance.save(any(Attendance.class)))
        .thenAnswer(
            invocation -> {
              Attendance session = invocation.getArgument(0);
              ReflectionTestUtils.setField(session, "id", 99L);
              open.set(session);
              return session;
            });

    mvc.perform(
            post("/api/attendance/punch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":7}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.action").value("PUNCH_IN"));
    mvc.perform(
            post("/api/attendance/punch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":7}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.action").value("PUNCH_OUT"))
        .andExpect(jsonPath("$.workedMinutes", greaterThanOrEqualTo(0)));
    assert open.get().getLogoutTime() != null;
  }

  @Test
  void monthlyPayrollUsesDailyFullHalfAndOvertimeBandsAndCanBeDisbursed() throws Exception {
    MockMvc payrollMvc =
        mvc(new SalaryController(new SalaryServiceImpl(salaries, users, attendance)));
    User employee = employee(12L, "ACTIVE", "2200.00");
    when(users.findAllByOnboardingStatus("ACTIVE")).thenReturn(List.of(employee));
    when(attendance.findOverlappingSessions(eq(12L), any(), any()))
        .thenReturn(
            List.of(
                session(employee, "2026-09-01T09:00:00", "2026-09-01T17:00:00"),
                session(employee, "2026-09-02T09:00:00", "2026-09-02T13:00:00"),
                session(employee, "2026-09-03T08:00:00", "2026-09-03T19:00:00")));
    when(salaries.findByUserIdAndSalaryMonthAndSalaryYear(12L, 9, 2026))
        .thenReturn(Optional.empty());
    when(salaries.save(any(Salary.class))).thenAnswer(invocation -> invocation.getArgument(0));

    payrollMvc
        .perform(
            post("/api/payroll/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"month\":9,\"year\":2026}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.employeeCount").value(1))
        .andExpect(jsonPath("$.fullPayDays").value(2))
        .andExpect(jsonPath("$.halfPayDays").value(1))
        .andExpect(jsonPath("$.unpaidDays").value(19))
        .andExpect(jsonPath("$.overtimeAchievements").value(1))
        .andExpect(jsonPath("$.employees[0].payableSalary").value(250.00));

    Salary calculated = new Salary(employee, 9, 2026);
    calculated.recalculate(
        22,
        2,
        1,
        19,
        1,
        new BigDecimal("1.00"),
        new BigDecimal("2200.00"),
        new BigDecimal("250.00"));
    ReflectionTestUtils.setField(calculated, "id", 501L);
    when(salaries.findWithLockByUserIdAndSalaryMonthAndSalaryYear(12L, 9, 2026))
        .thenReturn(Optional.of(calculated));
    when(payments.findByPayrollId(501L)).thenReturn(Optional.empty());
    when(payments.save(any(SalaryPayment.class)))
        .thenAnswer(
            invocation -> {
              SalaryPayment payment = invocation.getArgument(0);
              ReflectionTestUtils.setField(payment, "id", 601L);
              return payment;
            });
    MockMvc paymentMvc = mvc(new PaymentController(new PaymentServiceImpl(salaries, payments)));
    paymentMvc
        .perform(
            post("/api/payments/disburse")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\":12,\"month\":9,\"year\":2026}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("PENDING"))
        .andExpect(jsonPath("$.amount").value(250.00))
        .andExpect(jsonPath("$.maskedAccount").value("********9012"));
  }

  @Test
  void payrollRejectsOpenAttendanceInsteadOfSilentlyUnderpaying() throws Exception {
    MockMvc mvc = mvc(new SalaryController(new SalaryServiceImpl(salaries, users, attendance)));
    User employee = employee(33L, "ACTIVE", "3000.00");
    when(users.findAllByOnboardingStatus("ACTIVE")).thenReturn(List.of(employee));
    when(attendance.findOverlappingSessions(eq(33L), any(), any()))
        .thenReturn(List.of(new Attendance(employee, LocalDateTime.parse("2026-09-03T09:00:00"))));
    mvc.perform(
            post("/api/payroll/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"month\":9,\"year\":2026}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message", containsString("open attendance session")));
    verify(salaries, never()).save(any(Salary.class));
  }

  @Test
  void invalidOnboardingRequestGetsConsistentValidationError() throws Exception {
    MockMvc mvc = mvc(new HrmsController(new HrmsServiceImpl(users, new BCryptPasswordEncoder())));
    mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Request validation failed"))
        .andExpect(jsonPath("$.fieldErrors.email").exists());
    verifyNoInteractions(users);
  }

  private MockMvc mvc(Object controller) {
    return MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  private User employee(long id, String status, String salary) {
    User user =
        new User(
            "Test Employee",
            "employee" + id,
            "hashed-secret",
            "employee" + id + "@example.com",
            new BigDecimal(salary));
    ReflectionTestUtils.setField(user, "id", id);
    if ("ACTIVE".equals(status)) user.confirmOnboarding();
    user.setBankDetails("Example Bank", "123456789012", "EXAM0001234");
    user.setShift(ShiftType.OFFICE, null, null);
    return user;
  }

  private Attendance session(User user, String in, String out) {
    Attendance session = new Attendance(user, LocalDateTime.parse(in));
    session.punchOut(LocalDateTime.parse(out));
    return session;
  }
}
