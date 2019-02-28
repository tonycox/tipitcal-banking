package org.tonycox.banking.auth.service;

import org.tonycox.banking.auth.model.UserDao;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

public interface AuthService {
    UserDao signUp(SignUpServiceRequest request);
}
