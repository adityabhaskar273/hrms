# HRMS API

Spring Boot employee onboarding, attendance, monthly payroll, shifts, and payroll payment records.

## Local setup

Install PostgreSQL, start the server, and create a database named `hrms` (for example, with `createdb -U postgres hrms` or through pgAdmin). The local defaults are `postgres` / `postgres`; override them for any shared or deployed environment.

```sh
DB_URL='jdbc:postgresql://localhost:5432/hrms' \
DB_USERNAME=postgres DB_PASSWORD=postgres ./mvnw spring-boot:run
```

The service uses PostgreSQL. Create the `hrms` database before startup; Flyway creates the schema from versioned migrations, and Hibernate validates the mapped schema. Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` through environment variables outside local development. No JWT or authorization layer is configured.

## Workflow

1. `POST /api/employees` creates a pending employee. It accepts identity/contact details, monthly salary, bank details, and document references. Document references are metadata; this API does not upload files. Passwords are hashed before persistence and bank account numbers are masked in responses.
2. `POST /api/employees/{employeeId}/confirm-onboarding` activates the employee. Attendance is rejected until confirmation.
3. `POST /api/attendance/punch` toggles between punch-in and punch-out for the employee's open session. It supports overnight sessions; a session lasting longer than 20 hours is rejected for HR correction.
4. `POST /api/payroll/calculate` calculates payroll for a completed month across all active employees and saves each employee's result. Monday-Friday are counted as workdays; public holidays are not configured. Daily rules: zero recorded hours is unpaid, 0–under 8 hours is half pay, 8 hours or more is full pay. More than 10 hours is recorded as an overtime achievement; overtime hours are reported but are not added to salary.
5. `POST /api/payments/disburse` saves a pending payment request for an already-calculated payroll. It does not connect to a bank or transfer money. `POST /api/payments/{paymentId}/complete` records the external transaction reference after payment is made. Duplicate requests are rejected.

Custom shifts use `POST /api/shifts/assign`; overnight start/end times are supported when `shiftType` is `CUSTOM`. Attendance/payroll use recorded durations, not shift schedule compliance.

## Validation

Run `./mvnw test`. The controller-to-service tests exercise onboarding/confirmation, punch toggling, daily payroll bands, overtime recording, payment-request persistence, and validation errors.
