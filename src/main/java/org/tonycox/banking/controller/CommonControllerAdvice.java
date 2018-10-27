package org.tonycox.banking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tonycox.banking.dto.ErrorResponseDto;

@ControllerAdvice
public class CommonControllerAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ErrorResponseDto generalException(Exception ex) {
        return new ErrorResponseDto(HttpStatus.I_AM_A_TEAPOT.value(), ex.getMessage());
    }
}
