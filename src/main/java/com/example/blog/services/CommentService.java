package com.example.blog.services;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    CommentRepository commentRepository;

    public void create(Comment comment, Post post, User user){
        commentRepository.save(comment);
        post.getComments().add(comment);
        postService.save(post);
        user.getComments().add(comment);
        userService.save(user);
    }

    public List<Comment> findAll(){
        return commentRepository.findAll();
    }
}
