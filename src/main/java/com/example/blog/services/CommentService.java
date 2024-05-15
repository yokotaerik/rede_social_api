package com.example.blog.services;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.AddCommentDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.UserRole;
import com.example.blog.exceptions.EntityNotFoundException;
import com.example.blog.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    CommentRepository commentRepository;

    public void create(AddCommentDTO data, Post post, User user){

        LocalDate now = LocalDate.now();
        Comment comment = new Comment(null, data.content(), user, post, now);

        commentRepository.save(comment);
        post.getComments().add(comment);
        postService.save(post);
        user.getComments().add(comment);
        userService.save(user);
    }

    public void save(Comment comment){ commentRepository.save(comment); }

    public Comment findById(String id) throws EntityNotFoundException {
        return  commentRepository.findById(id).
            orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

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

    public void delete(Comment comment, User user) {

        if (comment.getAuthor().equals(user) || user.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(UserRole.ADMIN.name()))){

            comment.getAuthor().getComments().remove(comment);
            userService.save(comment.getAuthor());

            comment.getPost().getComments().remove(comment);
            postService.save(comment.getPost());

            List<User> likesOnComment = comment.getLikes();
            for(User user_who_likes : likesOnComment){
                user_who_likes.getLikedComments().remove(comment);
                userService.save(user_who_likes);
            }


            commentRepository.delete(comment);
        }
        else {
            throw new AccessDeniedException("You must be owner of comment");
        }

    }

}
