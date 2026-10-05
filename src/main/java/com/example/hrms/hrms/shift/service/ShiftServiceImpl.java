package com.example.hrms.hrms.shift.service;

import com.example.hrms.hrms.shift.dto.ShiftAssignmentRequest;
import com.example.hrms.hrms.shift.entity.ShiftType;
import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public String assignShift(ShiftAssignmentRequest shiftAssignmentRequest){
        if(shiftAssignmentRequest == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift assignment request cannot be empty");
        }

        User user = userRepository.findById(shiftAssignmentRequest.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ShiftType shiftType = shiftAssignmentRequest.getShiftType();
        if (shiftType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift type is required");
        }

        if(shiftType == ShiftType.CUSTOM){
            LocalTime customStartTime = shiftAssignmentRequest.getCustomShiftStartTime();

            LocalTime customEndTime = shiftAssignmentRequest.getCustomShiftEndTime();

            if(customStartTime == null || customEndTime == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Custom start time and end time are required");
            }

            if(!customEndTime.isAfter(customStartTime)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Custom end time must be after start time");
            }

            user.setCustomShiftStartTime(customStartTime);
            user.setCustomShiftEndTime(customEndTime);
        }else{
            user.setCustomShiftStartTime(null);
            user.setCustomShiftEndTime(null);
        }
        user.setShiftType(shiftType);
        userRepository.save(user);

        return "Shift assigned successfully";
    }

}
