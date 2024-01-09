package com.example.blog.services;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void save(Comment comment){ commentRepository.save(comment); }

    public List<Comment> findAll(){
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(String id) { return  commentRepository.findById(id);}

    public void likeOrDislike(Comment comment, User user){

        if(comment.getLikes().contains(user)){
            comment.getLikes().remove(user);
            user.getLikedComments().remove(comment);
        } else {
            comment.getLikes().add(user);
            user.getLikedComments().add(comment);
        }

        save(comment);
        userService.save(user);

    }
}
