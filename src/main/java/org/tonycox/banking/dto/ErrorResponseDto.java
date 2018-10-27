package org.tonycox.banking.dto;

import lombok.Value;

@Value
public class ErrorResponseDto {
    public ErrorResponseDto(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;
}
