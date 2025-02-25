package com.subodh.InternshipPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Response<T> {
    public Response(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    T entity;
    private String message;
    private HttpStatus status;
}
