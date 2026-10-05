package com.example.hrms.hrms.user_create.service;

import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.dto.CreateUserRequest;
import org.springframework.stereotype.Service;

@Service
public interface HrmsService {

    User createUser(CreateUserRequest createUserRequest);

    String login(String username, String password);

    String logout(String username, String password);
}
