package com.example.blog.controllers;

import com.example.blog.entities.user.User;
import com.example.blog.services.AuthService;
import com.example.blog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/like")
public class LikeController {

    private final AuthService authService;
    private final LikeService likeService;

    @Autowired
    public LikeController(AuthService authService, LikeService likeService) {
        this.authService = authService;
        this.likeService = likeService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> likeOrDislike(@PathVariable String id) throws Exception {
        User user = authService.getCurrentUser();
        likeService.LikeOrDislike(id, user);

        return ResponseEntity.ok("Successfully liked/disliked");
    }
}

