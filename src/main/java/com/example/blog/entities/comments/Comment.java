package com.example.blog.entities.comments;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UsernameDTO;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment implements Serializable {

    private String id;
    private String content;

    @DBRef(lazy = true)
    private User author;

    @DBRef(lazy = true)
    private Post post;
    private LocalDate createdAt;

    @DBRef(lazy = true)
    private List<User> likes = new ArrayList<>();

    public Comment(String id, String content, User author, Post post, LocalDate createdAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.post = post;
        this.createdAt = createdAt;
    }
}
