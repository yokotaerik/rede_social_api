package com.example.blog.controllers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.services.AuthService;
import com.example.blog.services.CommentService;
import com.example.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/like")
public class LikeController {

    private final AuthService authService;
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public LikeController(AuthService authService, PostService postService, CommentService commentService) {
        this.authService = authService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> likeOrDislike(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            try {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                User user = (User) authService.loadUserByUsername(username);

                Optional<Post> postOptional = postService.findById(id);
                Optional<Comment> commentOptional = commentService.findById(id);

                if (postOptional.isEmpty() && commentOptional.isEmpty()) {
                    return ResponseEntity.status(404).body("Neither post nor comment found with ID: " + id);
                }

                postOptional.ifPresent(post -> postService.likeOrDislike(post, user));
                commentOptional.ifPresent(comment -> commentService.likeOrDislike(comment, user));

                return ResponseEntity.ok("Like operation successful.");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error during like operation: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("User not authenticated.");
        }
    }
}

