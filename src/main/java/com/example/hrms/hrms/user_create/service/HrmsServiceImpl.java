package com.example.hrms.hrms.user_create.service;

import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.dto.CreateUserRequest;
import com.example.hrms.hrms.user_create.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HrmsServiceImpl implements HrmsService {

    private final UserRepository userRepository;


    @Override
    public String login(String username, String password){
       Optional<User> user = userRepository.findByUsername(username);
       if(user.isPresent() && user.get().getPassword().equals(password)){
        return "login successful";
       }
        return "Invalid username and password";
    }

    @Override
    public String logout(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password)){
            return "logout successful";
        }
        return "Invalid username and password";
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        if(createUserRequest==null){
            throw new RuntimeException("Create User Request cannot be empty");
        }
        String email = createUserRequest.getEmail();
        if(userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }


        User user = new User(createUserRequest.getName(),
                createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                email,
                createUserRequest.getMonthlySalary());
        return userRepository.save(user);

    }
}
