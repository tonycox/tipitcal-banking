package org.tonycox.banking.dto;

import lombok.Value;

@Value
public class ErrorResponseDto {
    private final Integer code;
    private final String message;
}
