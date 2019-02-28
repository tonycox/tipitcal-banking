package org.tonycox.banking.auth.api.dto;

import lombok.Value;

@Value
public class UserDto {
    private Long id;
    private String email;
}
