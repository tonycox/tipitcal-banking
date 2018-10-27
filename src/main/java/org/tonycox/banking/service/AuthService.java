package org.tonycox.banking.service;

import lombok.RequiredArgsConstructor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tonycox.banking.model.User;
import org.tonycox.banking.repository.UserRepository;
import org.tonycox.banking.request.SignUpRequest;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository repository;
    private final BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();

    public User signUp(SignUpRequest request) {
        String email = request.getEmail();
        if (repository.existsByEmail(email)) {
            throw new RuntimeException("user already exists");
        }
        return repository.save(new User()
                .setEmail(email)
                .setPassword(encryptor.encryptPassword(request.getPassword())));
    }
}
