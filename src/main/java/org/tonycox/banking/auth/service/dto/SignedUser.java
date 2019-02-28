package org.tonycox.banking.auth.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignedUser {
    private Long id;
    private String email;
}
