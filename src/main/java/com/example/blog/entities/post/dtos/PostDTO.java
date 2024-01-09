package com.example.blog.entities.post.dtos;

import com.example.blog.entities.comments.dtos.CommentDTO;

import java.time.LocalDate;
import java.util.List;

public record PostDTO(String id, String title, String content, String author, LocalDate createdAt, List<String> likes, List<CommentDTO> comments) {
}
