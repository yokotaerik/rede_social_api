package com.example.blog.controllers;

import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.CreateUserDTO;
import com.example.blog.entities.user.dtos.LoginDTO;
import com.example.blog.entities.user.dtos.LoginResponseDTO;
import com.example.blog.entities.user.dtos.UserInfo;
import com.example.blog.mappers.UserMapper;
import com.example.blog.services.AuthService;
import com.example.blog.services.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(UserService userService, AuthService authService, UserMapper userMapper) {
        this.userService = userService;
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserDTO data) throws BadRequestException {
        userService.create(data);

        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO credentials) {

        String token = authService.authenticateUser(credentials);

        if(token != null ){
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(""));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getInfo() throws Exception {

        User user = authService.getCurrentUser();

        UserInfo userInfo = userMapper.toUserInfo(user);

        return ResponseEntity.ok(userInfo);
    }
}
