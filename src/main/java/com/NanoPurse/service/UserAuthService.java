package com.NanoPurse.service;

import com.NanoPurse.dto.request.AuthenticationDto;
import com.NanoPurse.dto.request.RegistrationDto;
import com.NanoPurse.dto.response.AppResponse;

import javax.validation.Valid;

public interface UserAuthService {
    AppResponse<String> signUp( RegistrationDto registrationDto);

    AppResponse<String> signIn(AuthenticationDto authenticationDto);
}
