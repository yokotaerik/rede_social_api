package com.example.blog.entities.comments.dtos;

import com.example.blog.entities.user.User;

public record CommentDTO(String id, String author, String content) {
}
