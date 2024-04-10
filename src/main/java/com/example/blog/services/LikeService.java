package com.example.blog.services;

import com.example.blog.entities.user.User;
import com.example.blog.exceptions.EntityNotFoundException;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    public void LikeOrDislike(String id, User user) throws EntityNotFoundException {

        if(postRepository.findById(id).isPresent()){
            var post = postRepository.findById(id).get();
            if(post.getLikes().contains(user)){
                post.getLikes().remove(user);
                user.getLikedPosts().remove(post);
            } else {
                post.getLikes().add(user);
                user.getLikedPosts().add(post);
            }
            postRepository.save(post);
        } else if(commentRepository.findById(id).isPresent()){
            var comment = commentRepository.findById(id).get();
            if(comment.getLikes().contains(user)){
                comment.getLikes().remove(user);
                user.getLikedComments().remove(comment);
            } else {
                comment.getLikes().add(user);
                user.getLikedComments().add(comment);
            }
            commentRepository.save(comment);
        } else if(postRepository.findById(id).isEmpty() && commentRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Post or Comment not found");
        }
    }
}
