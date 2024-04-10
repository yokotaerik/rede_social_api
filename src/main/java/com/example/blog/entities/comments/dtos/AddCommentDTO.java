package com.example.blog.entities.comments.dtos;

import jakarta.validation.constraints.NotBlank;

public record AddCommentDTO(@NotBlank String content) {
}
