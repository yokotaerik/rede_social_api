package com.example.blog.services;

import com.example.blog.entities.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    com.example.blog.repositories.PostRepository PostRepository;

    public Post create(Post Post){
        return PostRepository.save(Post);
    }

    public List<Post> findAll(){
        return PostRepository.findAll();
    }
}
