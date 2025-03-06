package com.subodh.InternshipPortal.exceptions;

import lombok.Data;

/**
 * The type Exception response.
 */
@Data
public class ExceptionResponse  {
    private String message;
    private int status;

    /**
     * Instantiates a new Exception response.
     *
     * @param message the message
     * @param status  the status
     */
    public ExceptionResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }



}
