package com.example.blog.controllers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.AddCommentDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.AuthorDTO;
import com.example.blog.services.AuthService;
import com.example.blog.services.CommentService;
import com.example.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    AuthService authService;

    @Autowired
    PostService postService;


    @PostMapping("/add")
    public ResponseEntity create(@RequestBody AddCommentDTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = (User) authService.loadUserByUsername(userDetails.getUsername());

            Optional<Post> postOptional = postService.findById(data.post_id());

            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                LocalDate now = LocalDate.now();
                Comment comment = new Comment(null, data.content(), new AuthorDTO(user.getUsername()) , post.getId(), now);
                commentService.create(comment, post, user);

                return ResponseEntity.ok().body(comment);
            }
        }

        return ResponseEntity.badRequest().build();
    }
}