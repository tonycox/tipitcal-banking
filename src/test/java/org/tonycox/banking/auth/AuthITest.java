package org.tonycox.banking.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tonycox.banking.AbstractTest;
import org.tonycox.banking.auth.api.request.SignUpRequest;
import org.tonycox.banking.auth.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthITest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void registrationWillNotBeCompletedIfEmailIsWrong() {
        SignUpRequest request = new SignUpRequest()
                .setEmail("notemail.com")
                .setPassword("anypass");
        ResponseEntity<Object> entity = rest.postForEntity("/auth/sign-up", request, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    void commonRegistration() {
        SignUpRequest request = new SignUpRequest()
                .setEmail("e@mail.org")
                .setPassword("anypass");
        ResponseEntity<Object> entity = rest.postForEntity("/auth/sign-up", request, Object.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

}
