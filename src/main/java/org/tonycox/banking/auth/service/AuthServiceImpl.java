package org.tonycox.banking.auth.service;

import lombok.RequiredArgsConstructor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tonycox.banking.auth.model.UserDao;
import org.tonycox.banking.auth.repository.UserRepository;
import org.tonycox.banking.auth.service.dto.SignedUser;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final ModelMapper mapper;
    private final BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();

    public SignedUser signUp(SignUpServiceRequest request) {
        String email = request.getEmail();
        if (repository.existsByEmail(email)) {
            throw new RuntimeException("user already exists");
        }
        return mapper.map(repository.save(new UserDao()
                        .setEmail(email)
                        .setPassword(encryptor.encryptPassword(request.getPassword()))),
                SignedUser.class);
    }

    @Override
    public void removeUser(Long userId) {
        repository.deleteById(userId);
    }
}
