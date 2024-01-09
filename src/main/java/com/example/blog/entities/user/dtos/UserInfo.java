package com.example.blog.entities.user.dtos;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.CommentDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.PostDTO;
import com.example.blog.entities.user.User;

import java.time.LocalDate;
import java.util.List;

public record UserInfo(
        String id,
        String username,
        String email,
        String about,
        List<String> followers,
        List<String> following,
        List<PostDTO> likedPosts,
        List<CommentDTO> likedComments,
        List<PostDTO> posts,
        List<CommentDTO> comments,
        LocalDate createdAt
) {}