package com.example.hrms.hrms.shift.service;

import com.example.hrms.hrms.shift.dto.ShiftAssignmentRequest;
import com.example.hrms.hrms.shift.entity.ShiftType;
import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ShiftServiceImpl implements ShiftService {
  private final UserRepository users;

  public ShiftServiceImpl(UserRepository users) {
    this.users = users;
  }

  @Override
  @Transactional
  public String assignShift(ShiftAssignmentRequest request) {
    User user =
        users
            .findById(request.getUserId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    ShiftType type = request.getShiftType();
    if (type == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift type is required");
    var start = request.getCustomShiftStartTime();
    var end = request.getCustomShiftEndTime();
    if (type == ShiftType.CUSTOM) {
      if (start == null || end == null || start.equals(end))
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Custom shift needs different start and end times");
    } else {
      start = null;
      end = null;
    }
    user.setShift(type, start, end);
    users.save(user);
    return "Shift assigned successfully";
  }
}
