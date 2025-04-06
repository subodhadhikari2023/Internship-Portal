package com.subodh.InternshipPortal.exceptions;

/**
 * The type Invalid jwt token exception.
 */
public class InvalidJWTTokenException extends RuntimeException {
    /**
     * Instantiates a new Invalid jwt token exception.
     *
     * @param message the message
     */
    public InvalidJWTTokenException(String message) {
        super(message);
    }
}
