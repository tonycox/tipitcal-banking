package org.tonycox.banking.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    private String password;
}