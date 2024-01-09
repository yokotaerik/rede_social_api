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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
                          UserService userService, AuthService authService, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserDTO data) {
        if (authService.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        if (authService.loadUserByUsername(data.username()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        LocalDate now = LocalDate.now();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User user = new User(data.id(), data.username(), data.email(), encryptedPassword, now);
        userService.create(user);

        return ResponseEntity.ok("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponseDTO(null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> getInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            User user = (User) authService.loadUserByUsername(userDetails.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            UserInfo userInfo = userMapper.toUserInfo(user);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
