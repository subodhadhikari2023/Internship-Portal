package com.subodh.InternshipPortal.wrapper;

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

    public Response(T entity) {
        this.entity = entity;
    }

    T entity;
    private String message;
    private HttpStatus status;
}
