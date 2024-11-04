package com.damoa.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataFormatException extends RuntimeException {

    private HttpStatus status;

    public DataFormatException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}