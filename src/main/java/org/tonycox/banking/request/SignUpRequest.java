package org.tonycox.banking.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignUpRequest {
    private String email;
    private String password;
}
