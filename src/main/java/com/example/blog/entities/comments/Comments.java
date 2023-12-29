package com.example.blog.entities.comments;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.time.Instant;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comments implements Serializable {

    private String id;
    private String content;
    private User author;
    private Post post;
    private Instant createdAt;



}
