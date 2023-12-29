package com.example.blog.repositories;

import com.example.blog.entities.comments.Comments;
import com.example.blog.entities.post.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepositories extends MongoRepository<Comments, String> {
}
