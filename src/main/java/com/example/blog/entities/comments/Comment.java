package com.example.blog.entities.comments;

import com.example.blog.entities.user.dtos.UsernameDTO;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment implements Serializable {

    private String id;
    private String content;
    private String author;
    private String postId;
    private LocalDate createdAt;

}
