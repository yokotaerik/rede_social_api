package com.example.blog.entities.post;

import com.example.blog.entities.comments.Comment;
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
    private String author;
    private LocalDate createdAt;

    private List<String> likes = new ArrayList<>();


    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();
}
