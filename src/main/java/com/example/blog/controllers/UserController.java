package com.example.blog.controllers;

import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UserInfo;
import com.example.blog.services.AuthService;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @RequestMapping
    public ResponseEntity findAll() {
        List<User> users = userService.findAll();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity findAll(@PathVariable String username) {
        User user = (User) authService.loadUserByUsername(username);

        return ResponseEntity.ok().body(new UserInfo(user.getId(), user.getUsername(), user.getEmail(), user.getAbout(), user.getPosts()));
    }



}
