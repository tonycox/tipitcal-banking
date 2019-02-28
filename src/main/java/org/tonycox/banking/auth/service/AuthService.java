package org.tonycox.banking.auth.service;

import org.tonycox.banking.auth.service.dto.SignedUser;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

public interface AuthService {
    SignedUser signUp(SignUpServiceRequest request);

    void removeUser(Long userId);
}
