package com.subodh.InternshipPortal.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * The type Response.
 *
 * @param <T> the type parameter
 */
@Data
@AllArgsConstructor
public class Response<T>  {

    /**
     * Instantiates a new Response.
     *
     * @param message the message
     * @param status  the status
     */
    public Response(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Instantiates a new Response.
     *
     * @param entity the entity
     */
    public Response(T entity) {
        this.entity = entity;
    }

    /**
     * The Entity.
     */
    T entity;
    private String message;
    private HttpStatus status;


}
