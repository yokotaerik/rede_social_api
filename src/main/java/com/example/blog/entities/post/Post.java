package com.example.blog.entities.post;

import com.example.blog.entities.comments.dtos.CommentDTO;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.UsernameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Post implements Serializable {

    private String id;
    private String title;
    private String content;
    private UsernameDTO author;
    private LocalDate createdAt;

    private List<UsernameDTO> likes = new ArrayList<>();


    @DBRef(lazy = true)
    private List<CommentDTO> comments = new ArrayList<>();
}
