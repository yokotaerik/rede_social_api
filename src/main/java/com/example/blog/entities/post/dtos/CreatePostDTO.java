package com.example.blog.entities.post.dtos;

import com.example.blog.entities.user.User;

import java.time.LocalDate;

public record CreatePostDTO(String id, String tittle, String content) {
}
