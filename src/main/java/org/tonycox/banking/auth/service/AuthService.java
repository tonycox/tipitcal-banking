package org.tonycox.banking.auth.service;

import org.tonycox.banking.auth.model.User;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

public interface AuthService {
    User signUp(SignUpServiceRequest request);
}
