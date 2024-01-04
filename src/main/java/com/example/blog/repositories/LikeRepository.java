package com.example.blog.repositories;

import com.example.blog.entities.like.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeRepository extends MongoRepository<Like, String> {
    Like findByPostIdAndUserId(String postId, String userId);
}
