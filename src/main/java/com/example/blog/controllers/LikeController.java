package com.example.blog.controllers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.services.AuthService;
import com.example.blog.services.CommentService;
import com.example.blog.services.PostService;
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
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public LikeController(AuthService authService, PostService postService, CommentService commentService) {
        this.authService = authService;
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> likeOrDislike(@PathVariable String id) throws Exception {
                User user = authService.getCurrentUser();

                Post post = postService.findById(id);
                Comment comment = commentService.findById(id);


                postService.likeOrDislike(post, user);
                commentService.likeOrDislike(comment, user);

                return ResponseEntity.ok("Like operation successful.");
        }
}

