package org.tonycox.banking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tonycox.banking.dto.ErrorResponseDto;
import org.tonycox.banking.exception.ValidationException;

@ControllerAdvice
public class CommonControllerAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto generalException(Exception ex) {
        return new ErrorResponseDto(HttpStatus.I_AM_A_TEAPOT.value(), ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto validationException(Exception ex) {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
