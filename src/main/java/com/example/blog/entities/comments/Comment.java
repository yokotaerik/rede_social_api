package com.example.blog.entities.comments;

import com.example.blog.entities.user.dtos.UsernameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    private String id;
    private String content;
    private UsernameDTO author;
    private String postId;
    private LocalDate createdAt;



}
