package com.example.blog.repositories;

import com.example.blog.entities.comments.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comments, String> {
}
