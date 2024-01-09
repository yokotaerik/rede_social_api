package com.example.blog.repositories;

import com.example.blog.entities.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    UserDetails findByUsername(String username);

    User findByEmail(String email);

    @Query("{ 'username': { $regex: ?0, $options: 'i' } }")
    List<User> findByRegexInUsername(@Param("regex") String regex);
}
