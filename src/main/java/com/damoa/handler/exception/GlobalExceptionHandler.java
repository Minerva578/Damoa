package com.damoa.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 에러 처리
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(Exception e) {
        ModelAndView modelAndView = new ModelAndView("404"); // 404.html 뷰 이름
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleServerError(Exception e) {
//        ModelAndView modelAndView = new ModelAndView("500"); // 500.html 뷰 이름
//        modelAndView.addObject("errorMessage", e.getMessage());
//        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        return modelAndView;
//    }
}