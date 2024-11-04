package com.damoa.handler.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RuntimeException {
    // code = 401
    private HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
        super("로그인이 필요한 서비스 입니다.");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}