package com.example.blog.services;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.CreateUserDTO;
import com.example.blog.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }

    public User create(CreateUserDTO data) throws BadRequestException {

        if(userRepository.findByEmail(data.email()) != null){
            throw new BadRequestException("Email already exists");
        }

        if(userRepository.findByUsername(data.username()) != null){
            throw new BadRequestException("Username already exists");
        }

        LocalDate now = LocalDate.now();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        User user = new User(data.id(), data.username(), data.email(), encryptedPassword, now);

        userRepository.save(user);

        return user;
    }

    public Optional<User> findById(String id) { return userRepository.findById(id); }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void follow(User follower, User followed) {

        if (followed.getFollowers().contains(follower)) {
            followed.removeFollower(follower);
            follower.removeFollowing(followed);
        } else {
            follower.addFollowing(followed);
            followed.addFollower(follower);
        }

        save(followed);
        save(follower);
    }

    public List<Post> getFeed(User user){
        List<User> following = user.getFollowing();

        return following
                .stream()
                .flatMap(u -> u.getPosts().stream())
                .collect(Collectors.toList());
    }

    public List<User> findUsersByUsername(String username){
        return userRepository.findByRegexInUsername(username);
    }


}
