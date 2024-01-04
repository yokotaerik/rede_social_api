package com.example.blog.entities.post;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.comments.dtos.CommentDTO;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {

    private String id;
    private String title;
    private String content;
    private AuthorDTO author;
    private LocalDate createdAt;
    private Integer like = 0;
    private List<CommentDTO> comments = new ArrayList<>();
}
