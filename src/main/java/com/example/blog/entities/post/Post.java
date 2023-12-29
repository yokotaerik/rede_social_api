package com.example.blog.entities.post;

import com.example.blog.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {

    private String id;
    private String tittle;
    private String content;
    private User author;
    private Instant createdAt;
}
