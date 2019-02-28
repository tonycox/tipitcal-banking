package org.tonycox.banking.auth.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class SignUpRequest {
    @Email
    private String email;
    @NotEmpty
    private String password;
}
