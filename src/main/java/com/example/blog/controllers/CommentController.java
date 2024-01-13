package com.example.blog.controllers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.AddCommentDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.UserRole;
import com.example.blog.services.AuthService;
import com.example.blog.services.CommentService;
import com.example.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

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
    public ResponseEntity<String> create(@RequestBody AddCommentDTO data, @PathVariable String postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = (User) authService.loadUserByUsername(userDetails.getUsername());

            Optional<Post> postOptional = postService.findById(postId);

            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                LocalDate now = LocalDate.now();
                Comment comment = new Comment(null, data.content(), user, post, now);
                commentService.create(comment, post, user);

                return ResponseEntity.ok("Successfully requested.");
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable String id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = (User) authService.loadUserByUsername(username);

            Optional<Comment> optionalComment = commentService.findById(id);

            if(optionalComment.isPresent()){
                Comment comment = optionalComment.get();
                if (comment.getAuthor().equals(user) || user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(UserRole.ADMIN.name()))){
                    commentService.delete(comment);
                    return ResponseEntity.ok().body("Comment successfully deleted");
                } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this post");
            } else return ResponseEntity.notFound().build();

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}
