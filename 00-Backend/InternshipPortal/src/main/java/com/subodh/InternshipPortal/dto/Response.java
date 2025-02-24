package com.subodh.InternshipPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Response {
    private String message;
    private HttpStatus status;
}
