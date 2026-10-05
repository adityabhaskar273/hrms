package com.example.hrms.hrms.user_create.Entity;


import com.example.hrms.hrms.shift.entity.ShiftType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String username;
    @Getter
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlySalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type")
    private ShiftType shiftType;

    @Column(name = "custom_shift_start_time")
    private LocalTime customShiftStartTime;

    @Column(name = "custome_shift_end_time")
    private LocalTime customShiftEndTime;



    public User(String name, String username, String password, String email, BigDecimal monthlySalary){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.monthlySalary = monthlySalary;
    }

}
