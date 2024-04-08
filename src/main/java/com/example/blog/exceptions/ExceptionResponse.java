package com.example.blog.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse implements Serializable {

    // Classe para padronizar as excecoes

    private Date timeStamp;
    private String message;
    private String details;
}