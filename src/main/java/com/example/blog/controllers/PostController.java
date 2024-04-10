package com.example.blog.controllers;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.CreatePostDTO;
import com.example.blog.entities.post.dtos.PostDTO;
import com.example.blog.entities.user.User;
import com.example.blog.exceptions.EntityNotFoundException;
import com.example.blog.mappers.PostMapper;
import com.example.blog.services.AuthService;
import com.example.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<String> create(@RequestBody CreatePostDTO data) throws Exception {
        User user = authService.getCurrentUser();
        postService.create(data, user);

        return ResponseEntity.ok("Successfully requested");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> findAll() {
        List<Post> posts = postService.findAll();
        List<PostDTO> postDTOs = posts.stream().map(postMapper::toDTO).collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable String id) throws EntityNotFoundException {
        Post post = postService.findById(id);

        return ResponseEntity.ok(postMapper.toDTO(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) throws Exception {

        User user = authService.getCurrentUser();

        Post post = postService.findById(id);

        postService.delete(post, user);
        return ResponseEntity.ok("Post deleted successfully");
    }

}