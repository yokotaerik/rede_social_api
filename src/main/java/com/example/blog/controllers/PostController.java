package com.example.blog.controllers;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.CreatePostDTO;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UsernameDTO;
import com.example.blog.services.AuthService;
import com.example.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    AuthService authService;

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody CreatePostDTO data){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String username = userDetails.getUsername();

            User user = (User) authService.loadUserByUsername(username);

            LocalDate now = LocalDate.now();

            Post post = new Post(null, data.title(), data.content(), new UsernameDTO(user.getUsername()), now, null , null);

            postService.create(post, user);



            return ResponseEntity.ok().body(post);

        }

        return  ResponseEntity.badRequest().build();

    }

    @GetMapping("/all")
    public ResponseEntity findAll(){
        List<Post> posts = postService.findAll();

        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable String id) {
        Optional<Post> post = postService.findById(id);

        if (post.isPresent()) {
            return ResponseEntity.ok().body(post.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity likeOrDislike(@PathVariable String postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                User user = (User) authService.loadUserByUsername(username);

                Optional<Post> postOptional = postService.findById(postId);

                postService.likeOrDislike(postOptional.get(), user);



                return ResponseEntity.ok("Like operation successful.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error during like operation: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("User not authenticated.");
        }
    }
}

