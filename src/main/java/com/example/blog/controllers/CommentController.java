package com.example.blog.controllers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.AddCommentDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.services.AuthService;
import com.example.blog.services.CommentService;
import com.example.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, AuthService authService, PostService postService) {
        this.commentService = commentService;
        this.authService = authService;
        this.postService = postService;
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<String> create(@RequestBody AddCommentDTO data, @PathVariable String postId) throws Exception {
            User user = authService.getCurrentUser();

            Post  post = postService.findById(postId);
            commentService.create(data, post, user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Comment created");
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable String id) throws Exception {
            User user = authService.getCurrentUser();

            Comment comment = commentService.findById(id);

            commentService.delete(comment, user);


            return ResponseEntity.ok().body("Comment successfully deleted");
    }
}
