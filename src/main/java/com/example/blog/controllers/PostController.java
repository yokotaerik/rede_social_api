package com.example.blog.controllers;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.CreatePostDTO;
import com.example.blog.entities.post.dtos.PostDTO;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.UserRole;
import com.example.blog.mappers.PostMapper;
import com.example.blog.services.AuthService;
import com.example.blog.services.PostService;
import com.example.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {

    private final AuthService authService;
    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public PostController(AuthService authService, PostService postService, PostMapper postMapper) {
        this.authService = authService;
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreatePostDTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = (User) authService.loadUserByUsername(username);

            LocalDate now = LocalDate.now();
            Post post = new Post(null, data.title(), data.content(), user, now, null, null);

            postService.create(post, user);

            return ResponseEntity.ok("Successfully requested");
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> findAll() {
        List<Post> posts = postService.findAll();
        List<PostDTO> postDTOs = posts.stream().map(postMapper::toDTO).collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable String id) {
        Optional<Post> post = postService.findById(id);

        return post.map(p -> ResponseEntity.ok(postMapper.toDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = (User) authService.loadUserByUsername(username);

            Optional<Post> optionalPost = postService.findById(id);

            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                if (post.getAuthor().getUsername().equals(username) || user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(UserRole.ADMIN.name()))) {
                    postService.delete(post);
                    return ResponseEntity.ok("Post deleted successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this post");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

}