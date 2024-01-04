package com.example.blog.controllers;

import com.example.blog.entities.like.dtos.LikeDTO;
import com.example.blog.entities.user.User;
import com.example.blog.services.AuthService;
import com.example.blog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private AuthService authService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> likeOrDislike(@PathVariable String postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                User user = (User) authService.loadUserByUsername(username);

                LikeDTO data = new LikeDTO(postId, user.getId());
                likeService.likeOrDislike(data);

                return ResponseEntity.ok("Like operation successful.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error during like operation: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("User not authenticated.");
        }
    }
}