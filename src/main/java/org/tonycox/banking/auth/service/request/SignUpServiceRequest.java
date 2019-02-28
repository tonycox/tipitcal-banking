package org.tonycox.banking.auth.service.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignUpServiceRequest {
    private String email;
    private String password;
}
