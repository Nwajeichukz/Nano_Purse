package com.NanoPurse.controller;

import com.NanoPurse.dto.request.AuthenticationDto;
import com.NanoPurse.dto.request.RegistrationDto;
import com.NanoPurse.dto.response.AppResponse;
import com.NanoPurse.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    @PostMapping("register")
    public AppResponse<String> signUp(@Valid @RequestBody RegistrationDto registrationDto){
        return userAuthService.signUp(registrationDto);
    }


    @PostMapping("login")
    public AppResponse<String> signIn(@Valid @RequestBody AuthenticationDto authenticationDto){
        return userAuthService.signIn(authenticationDto);
    }

}
