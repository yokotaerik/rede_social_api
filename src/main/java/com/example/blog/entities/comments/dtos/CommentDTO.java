package com.example.blog.entities.comments.dtos;

import java.time.LocalDate;
import java.util.List;

public record CommentDTO(String id, String content, String author, String postId , LocalDate createdAt, List<String> likes) {
}
