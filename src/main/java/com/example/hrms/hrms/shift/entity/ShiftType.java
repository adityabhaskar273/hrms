package com.example.hrms.hrms.shift.entity;


import lombok.Getter;

import java.time.LocalTime;

@Getter
public enum ShiftType {

    OFFICE(LocalTime.of(9, 0), LocalTime.of(18, 0)),
    FACTORY(LocalTime.of(9,0), LocalTime.of(18, 0)),
    FIELD(LocalTime.of(9, 0), LocalTime.of(18, 0)),
    CUSTOM;

    private final LocalTime defaultStartTime;
    private final LocalTime defaultEndTime;

    ShiftType(LocalTime defaultStartTime, LocalTime defaultEndTime){
        this.defaultStartTime = defaultStartTime;
        this.defaultEndTime = defaultEndTime;
    }

    ShiftType(){
        this.defaultStartTime = null;
        this.defaultEndTime = null;

    }

    public boolean isCustomShift(){
        return this == CUSTOM;
    }

}
