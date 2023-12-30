package com.example.blog.entities.post;

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

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post implements Serializable {

    private String id;
    private String tittle;
    private String content;
    private AuthorDTO author;
    private LocalDate createdAt;
}
