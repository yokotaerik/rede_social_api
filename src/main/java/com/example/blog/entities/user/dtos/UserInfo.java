package com.example.blog.entities.user.dtos;

import com.example.blog.entities.post.Post;

import java.util.List;

public record UserInfo(String id, String username, String email, String about, List<Post> posts, List<Post> likes) {
}
