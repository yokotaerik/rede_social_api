package com.example.blog.repositories;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositories extends MongoRepository<Post, String> {
}
