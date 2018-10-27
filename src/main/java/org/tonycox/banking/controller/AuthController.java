package org.tonycox.banking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tonycox.banking.dto.UserDto;
import org.tonycox.banking.request.SignUpRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/sign-up")
    public UserDto signUp(@RequestBody SignUpRequest request) {
        return null;
    }
}
