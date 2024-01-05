package com.example.blog.services;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }

    public void create(User user){
        userRepository.save(user);
    }

    public Optional<User> findById(String id) { return userRepository.findById(id); }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
