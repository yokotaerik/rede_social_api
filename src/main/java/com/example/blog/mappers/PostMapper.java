package com.example.blog.mappers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.CommentDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.PostDTO;
import org.springframework.stereotype.Component;
import com.example.blog.entities.user.User;

import java.util.stream.Collectors;

@Component
public class PostMapper {

    public PostDTO toDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                extractUsername(post.getAuthor()),
                post.getCreatedAt(),
                post.getLikes().stream().map(User::getUsername).collect(Collectors.toList()),
                post.getComments().stream().map(this::toCommentDTO).collect(Collectors.toList())
        );
    }

    public PostDTO toDTOWithoutComments(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                extractUsername(post.getAuthor()),
                post.getCreatedAt(),
                post.getLikes().stream().map(User::getUsername).collect(Collectors.toList()),
                null
        );
    }

    private String extractUsername(User user) {
        return (user != null) ? user.getUsername() : null;
    }

    public CommentDTO toCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                extractUsername(comment.getAuthor()),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getLikes().stream().map(User::getUsername).collect(Collectors.toList())
        );
    }
}
