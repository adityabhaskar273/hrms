package com.example.hrms.hrms.salary.service;

import com.example.hrms.hrms.login.attendance.entity.Attendance;
import com.example.hrms.hrms.login.attendance.repository.AttendanceRepository;
import com.example.hrms.hrms.salary.dto.*;
import com.example.hrms.hrms.salary.entity.Salary;
import com.example.hrms.hrms.salary.repository.SalaryRepository;
import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SalaryServiceImpl implements SalaryService {
  private static final BigDecimal EIGHT = BigDecimal.valueOf(8), TEN = BigDecimal.TEN;
  private final SalaryRepository salaries;
  private final UserRepository users;
  private final AttendanceRepository attendance;

  public SalaryServiceImpl(SalaryRepository s, UserRepository u, AttendanceRepository a) {
    salaries = s;
    users = u;
    attendance = a;
  }

  @Override
  @Transactional
  public PayrollResponse calculateSalary(SalaryCalculationRequest request) {
    YearMonth period;
    try {
      period = YearMonth.of(request.getYear(), request.getMonth());
    } catch (DateTimeException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payroll period");
    }
    if (!period.isBefore(YearMonth.now()))
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Payroll can only be calculated for a completed month");
    LocalDateTime from = period.atDay(1).atStartOfDay(),
        to = period.plusMonths(1).atDay(1).atStartOfDay();
    List<User> active = users.findAllByOnboardingStatus("ACTIVE");
    List<SalaryResponse> results = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;
    int full = 0, half = 0, unpaid = 0, ot = 0;
    for (User user : active) {
      List<Attendance> sessions = attendance.findOverlappingSessions(user.getId(), from, to);
      if (sessions.stream().anyMatch(s -> s.getLogoutTime() == null))
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Employee " + user.getId() + " has an open attendance session in the payroll month");
      Map<LocalDate, Long> minutes = aggregateMinutesByDay(sessions, period);
      int workdays = 0, fullDays = 0, halfDays = 0, unpaidDays = 0, overtimeDays = 0;
      long overtimeMinutes = 0;
      for (LocalDate day = period.atDay(1);
          !day.isAfter(period.atEndOfMonth());
          day = day.plusDays(1)) {
        if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY)
          continue;
        workdays++;
        long mins = minutes.getOrDefault(day, 0L);
        double hours = mins / 60.0;
        if (mins >= 480) fullDays++;
        else if (mins > 0) halfDays++;
        else unpaidDays++;
        if (mins > 600) {
          overtimeDays++;
          overtimeMinutes += mins - 600;
        }
      }
      BigDecimal daily =
          user.getMonthlySalary().divide(BigDecimal.valueOf(workdays), 8, RoundingMode.HALF_UP);
      BigDecimal payable =
          daily
              .multiply(
                  BigDecimal.valueOf(fullDays)
                      .add(BigDecimal.valueOf(halfDays).multiply(new BigDecimal("0.5"))))
              .setScale(2, RoundingMode.HALF_UP);
      Salary record =
          salaries
              .findWithLockByUserIdAndSalaryMonthAndSalaryYear(
                  user.getId(), period.getMonthValue(), period.getYear())
              .orElseGet(() -> new Salary(user, period.getMonthValue(), period.getYear()));
      if (record.isPaymentRequestCreated())
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Payroll already has a payment request and cannot be recalculated");
      record.recalculate(
          workdays,
          fullDays,
          halfDays,
          unpaidDays,
          overtimeDays,
          BigDecimal.valueOf(overtimeMinutes)
              .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP),
          user.getMonthlySalary(),
          payable);
      results.add(toResponse(salaries.save(record)));
      total = total.add(payable);
      full += fullDays;
      half += halfDays;
      unpaid += unpaidDays;
      ot += overtimeDays;
    }
    return new PayrollResponse(
        period.getMonthValue(),
        period.getYear(),
        results.size(),
        full,
        half,
        unpaid,
        ot,
        total.setScale(2, RoundingMode.HALF_UP),
        List.copyOf(results));
  }

  @Override
  @Transactional(readOnly = true)
  public SalaryResponse getSalary(long id, int month, int year) {
    if (month < 1 || month > 12 || year < 2000)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid payroll period");
    return salaries
        .findByUserIdAndSalaryMonthAndSalaryYear(id, month, year)
        .map(this::toResponse)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll record not found"));
  }

  private SalaryResponse toResponse(Salary s) {
    return new SalaryResponse(
        s.getUser().getId(),
        s.getUser().getName(),
        s.getSalaryMonth(),
        s.getSalaryYear(),
        s.getTotalWorkingDays(),
        s.getFullPayDays(),
        s.getHalfPayDays(),
        s.getUnpaidDays(),
        s.getOvertimeAchievementDays(),
        s.getOvertimeHours(),
        s.getMonthlySalary(),
        s.getPayableSalary(),
        s.getCalculatedAt());
  }

  private Map<LocalDate, Long> aggregateMinutesByDay(List<Attendance> sessions, YearMonth month) {
    Map<LocalDate, List<Range>> ranges = new HashMap<>();
    LocalDateTime monthStart = month.atDay(1).atStartOfDay(),
        monthEnd = month.plusMonths(1).atDay(1).atStartOfDay();
    for (Attendance s : sessions) {
      if (s.getLogoutTime() == null) continue;
      LocalDateTime start = s.getLoginTime().isBefore(monthStart) ? monthStart : s.getLoginTime(),
          end = s.getLogoutTime().isAfter(monthEnd) ? monthEnd : s.getLogoutTime();
      while (start.isBefore(end)) {
        LocalDate date = start.toLocalDate();
        LocalDateTime boundary = date.plusDays(1).atStartOfDay();
        LocalDateTime clippedEnd = end.isBefore(boundary) ? end : boundary;
        ranges.computeIfAbsent(date, k -> new ArrayList<>()).add(new Range(start, clippedEnd));
        start = clippedEnd;
      }
    }
    Map<LocalDate, Long> totals = new HashMap<>();
    ranges.forEach(
        (date, list) -> {
          list.sort(Comparator.comparing(Range::start));
          long sum = 0;
          LocalDateTime a = null, b = null;
          for (Range r : list) {
            if (a == null) {
              a = r.start;
              b = r.end;
            } else if (!r.start.isAfter(b)) {
              if (r.end.isAfter(b)) b = r.end;
            } else {
              sum += Duration.between(a, b).toMinutes();
              a = r.start;
              b = r.end;
            }
          }
          if (a != null) sum += Duration.between(a, b).toMinutes();
          totals.put(date, sum);
        });
    return totals;
  }

  private record Range(LocalDateTime start, LocalDateTime end) {}
}
