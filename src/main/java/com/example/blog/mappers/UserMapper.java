package com.example.blog.mappers;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private PostMapper postMapper;

    public UserInfo toUserInfo(User user) {
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAbout(),
                user.getFollowers().stream().map(User::getUsername).collect(Collectors.toList()),
                user.getFollowing().stream().map(User::getUsername).collect(Collectors.toList()),
                user.getLikedPosts().stream().map(post -> postMapper.toDTO(post)).collect(Collectors.toList()),
                user.getLikedComments().stream().map(comment -> postMapper.toCommentDTO(comment)).collect(Collectors.toList()),
                user.getPosts().stream().map(post -> postMapper.toDTO(post)).collect(Collectors.toList()),
                user.getComments().stream().map(comment -> postMapper.toCommentDTO(comment)).collect(Collectors.toList()),
                user.getCreatedAt()
        );
    }
}
