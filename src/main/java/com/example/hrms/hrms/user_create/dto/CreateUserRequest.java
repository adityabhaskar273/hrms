package com.example.hrms.hrms.user_create.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Username is Required")
    private String username;

    @NotBlank(message = "Password is Required" )
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email")
    private String email;

    @NotNull(message = "Monthly Salary is Required")
    @DecimalMin(value = "0.01", message = "Monthly Salary must be greater than zero")
    private BigDecimal monthlySalary;

}
