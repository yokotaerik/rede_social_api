package com.example.blog.controllers;

import com.example.blog.entities.user.User;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping
    public ResponseEntity findAll() {
        List<User> users = userService.findAll();

        return ResponseEntity.ok().body(users);
    }


}
