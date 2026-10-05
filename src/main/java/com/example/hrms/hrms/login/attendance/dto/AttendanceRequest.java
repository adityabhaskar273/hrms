package com.example.hrms.hrms.login.attendance.dto;


import jakarta.validation.constraints.NotNull;



public class AttendanceRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    public Long getUserId(){
        return userId;
    }

    public void setUserId(Long userId){
        this.userId= userId;
    }

}
