package org.tonycox.banking.auth.api;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tonycox.banking.auth.api.dto.UserDto;
import org.tonycox.banking.auth.api.request.SignUpRequest;
import org.tonycox.banking.auth.service.AuthService;
import org.tonycox.banking.auth.service.dto.SignedUser;
import org.tonycox.banking.auth.service.request.SignUpServiceRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final ModelMapper mapper;

    @PostMapping("/sign-up")
    public UserDto signUp(@RequestBody @Valid SignUpRequest request) {
        SignedUser user = service.signUp(mapper.map(request, SignUpServiceRequest.class));
        return new UserDto(user.getId(), user.getEmail());
    }
}
