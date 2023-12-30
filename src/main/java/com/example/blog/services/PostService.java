package com.example.blog.services;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    public void create(Post post, User user){
        postRepository.save(post);
        user.getPosts().add(post);
        userService.save(user);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }
}
