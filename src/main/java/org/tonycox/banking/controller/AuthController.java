package org.tonycox.banking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tonycox.banking.dto.UserDto;
import org.tonycox.banking.model.User;
import org.tonycox.banking.request.SignUpRequest;
import org.tonycox.banking.service.AuthService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/sign-up")
    public UserDto signUp(@RequestBody SignUpRequest request) {
        User user = service.signUp(request);
        return new UserDto(user.getId(), user.getEmail());
    }
}
