package com.example.blog.controllers;

import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.CreateUserDTO;
import com.example.blog.entities.user.dtos.LoginDTO;
import com.example.blog.entities.user.dtos.LoginResponseDTO;
import com.example.blog.entities.user.dtos.UserInfo;
import com.example.blog.infra.security.TokenService;
import com.example.blog.mappers.UserMapper;
import com.example.blog.services.AuthService;
import com.example.blog.services.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UserService userService, AuthService authService, UserMapper userMapper) {
        this.userService = userService;
        this.authService = authService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserDTO data) throws BadRequestException {
        userService.create(data);

        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO credentials) {
        try {
            // Verifica as credenciais, se forem validas ele envia o token, caso contrário retorna um unauthorized
            var usernamePassword = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cannot authenticate user ");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getInfo() throws Exception {

        User user = authService.getCurrentUser();

        UserInfo userInfo = userMapper.toUserInfo(user);

        return ResponseEntity.ok(userInfo);
    }
}
