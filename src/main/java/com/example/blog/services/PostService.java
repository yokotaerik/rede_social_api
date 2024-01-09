package com.example.blog.services;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UsernameDTO;
import com.example.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Post> findById(String id) { return postRepository.findById(id); }

    public void save(Post post) {  postRepository.save(post); }

    public void likeOrDislike(Post post, User user){

        if(post.getLikes().contains(user)){
            post.getLikes().remove(user);
            user.getLikedPosts().remove(post);
        } else {
            post.getLikes().add(user);
            user.getLikedPosts().add(post);
        }

        save(post);
        userService.save(user);

    }
}
