package com.example.blog.controllers;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UserInfo;
import com.example.blog.services.AuthService;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfo> findByUsername(@PathVariable String username) {
        User user = (User) authService.loadUserByUsername(username);
        UserInfo userInfo = new UserInfo(user.getId(), user.getUsername(), user.getEmail(), user.getAbout(), user.getPosts(), user.getLikedPosts(), user.getFollowers().stream().map(User::getUsername).collect(Collectors.toList()),
                user.getFollowing().stream().map(User::getUsername).collect(Collectors.toList()));
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/follow/{usernameFollowed}")
    public ResponseEntity<String> follow(@PathVariable String usernameFollowed) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String usernameFollower = userDetails.getUsername();
                User follower = (User) authService.loadUserByUsername(usernameFollower);
                User followed = (User) authService.loadUserByUsername(usernameFollowed);

                userService.follow(follower, followed);

                return ResponseEntity.ok("Follow operation successful.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error during follow operation: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("User not authenticated.");
        }
    }

    @GetMapping("/feed")
    public ResponseEntity getFeed() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = (User) authService.loadUserByUsername(username);
            List<Post> feed = userService.getFeed(user);
            return ResponseEntity.ok(feed);
        }

        return ResponseEntity.status(401).body(null);
    }
}