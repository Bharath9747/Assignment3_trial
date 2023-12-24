package com.accolite.app.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionHandler extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;

    public ExceptionHandler(String message) {
        super();
        this.message = message;
    }
}
