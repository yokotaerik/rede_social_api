package com.example.blog.controllers;

import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.CreateUserDTO;
import com.example.blog.entities.user.dtos.LoginDTO;
import com.example.blog.entities.user.dtos.LoginResponseDTO;
import com.example.blog.infra.security.TokenService;
import com.example.blog.services.AuthService;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody CreateUserDTO data){

        User byEmail = authService.findByEmail(data.email());

        UserDetails byUsername = authService.loadUserByUsername(data.username());

        if (byEmail != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        if (byUsername != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }


        LocalDate now = LocalDate.now();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User user = new User(data.id() , data.username(), data.email(), encryptedPassword, now);

        userService.create(user);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generatedToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

}
