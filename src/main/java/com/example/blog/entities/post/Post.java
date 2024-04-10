package com.example.blog.entities.post;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.user.User;
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
public class Post implements Serializable {

    private String id;
    private String title;
    private String content;

    @DBRef(lazy = true)
    private User author;
    private LocalDate createdAt;

    @DBRef(lazy = true)
    private List<User> likes = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content, User author, LocalDate createdAt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }
}
