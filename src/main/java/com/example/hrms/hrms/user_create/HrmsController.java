package com.example.hrms.hrms.user_create;

import com.example.hrms.hrms.user_create.Entity.User;
import com.example.hrms.hrms.user_create.dto.CreateUserRequest;
import com.example.hrms.hrms.user_create.dto.LoginUserRequest;
import com.example.hrms.hrms.user_create.service.HrmsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HrmsController {

    private final HrmsService hrmsService;

    public HrmsController(HrmsService hrmsService){
        this.hrmsService= hrmsService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid  @RequestBody CreateUserRequest createUserRequest){
        User user = hrmsService.createUser(createUserRequest);
        if(user!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body("User has been created");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User cannot be created");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserRequest loginUserRequest){
        String respBody =  hrmsService.login(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(respBody);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LoginUserRequest loginUserRequest){
        String respBody =  hrmsService.logout(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(respBody);
    }


}
