package com.example.blog.controllers;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.PostDTO;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.AboutMeDTO;
import com.example.blog.entities.user.dtos.UserInfo;
import com.example.blog.mappers.PostMapper;
import com.example.blog.mappers.UserMapper;
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

    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Autowired
    public UserController(UserService userService, AuthService authService, UserMapper userMapper, PostMapper postMapper) {
        this.userService = userService;
        this.authService = authService;
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfo> findByUsername(@PathVariable String username) {
        User user = (User) authService.loadUserByUsername(username);
        UserInfo userInfo = userMapper.toUserInfo(user);
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
    public ResponseEntity<List<PostDTO>> getFeed() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = (User) authService.loadUserByUsername(username);
            List<Post> posts = userService.getFeed(user);
            List<PostDTO> feed = posts.stream().map(postMapper::toDTO).collect(Collectors.toList());

            return ResponseEntity.ok(feed);
        }

        return ResponseEntity.status(401).body(null);
    }

    @PatchMapping("/about")
    public ResponseEntity<String> patchAboutMe(@RequestBody AboutMeDTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = (User) authService.loadUserByUsername(username);
            user.setAbout(data.about());
            userService.save(user);
        }
        return ResponseEntity.ok().body("Successfully request");
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<List<String>> findUsersByUsername(@PathVariable String username){
        List<User> users = userService.findUsersByUsername(username);

        List<String> names = users.stream().map(User::getUsername).collect(Collectors.toList());

        return ResponseEntity.ok().body(names);
    }
}