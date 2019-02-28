package org.tonycox.banking.auth.service;

import lombok.RequiredArgsConstructor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tonycox.banking.auth.model.User;
import org.tonycox.banking.auth.repository.UserRepository;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();

    public User signUp(SignUpServiceRequest request) {
        String email = request.getEmail();
        if (repository.existsByEmail(email)) {
            throw new RuntimeException("user already exists");
        }
        return repository.save(new User()
                .setEmail(email)
                .setPassword(encryptor.encryptPassword(request.getPassword())));
    }
}
