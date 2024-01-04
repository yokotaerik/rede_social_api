package com.example.blog.entities.like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Like implements Serializable {
    @Id
    private String id;
    private String postId;
    private String userId;
}
