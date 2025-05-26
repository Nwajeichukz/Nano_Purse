package com.NanoPurse.service.impl;

import com.NanoPurse.dto.request.AuthenticationDto;
import com.NanoPurse.dto.request.RegistrationDto;
import com.NanoPurse.dto.response.AppResponse;
import com.NanoPurse.exception.ApiException;
import com.NanoPurse.model.Role;
import com.NanoPurse.model.User;
import com.NanoPurse.model.Wallet;
import com.NanoPurse.repository.RoleRepository;
import com.NanoPurse.repository.UserRepository;
import com.NanoPurse.repository.WalletRepository;
import com.NanoPurse.service.UserAuthService;
import com.NanoPurse.service.jwt.JwtService;
import com.NanoPurse.service.jwt.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final JwtService jwtService;

    private final RoleRepository roleRepository;

    private  final WalletRepository walletRepository;

    private final MyUserDetailsService myUserDetailsService;

    @Override
    public AppResponse<String> signUp (RegistrationDto registrationDto) {
        emailCheck(registrationDto.getEmail());

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            return new AppResponse<>(-1, "Passwords do not match");
        }

        User user = createUserEntity(registrationDto);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCurrency(registrationDto.getCurrency());

        Wallet savedUser = walletRepository.save(wallet);

        return new AppResponse<>(0, savedUser.getUser().getEmail() + " your account has been created");
    }


    public AppResponse<String> signIn(AuthenticationDto authenticationDto) {

        var user = myUserDetailsService.loadUserByUsername(authenticationDto.getEmail());

        if(!passwordEncoder.matches(authenticationDto.getPassword(), user.getPassword()))
            return new AppResponse<>(-1, "wrong email or password");

        String token = jwtService.generateToken(user);

        return new AppResponse<>(0, "successful signin", token);
    }

    private void emailCheck(String email){
        boolean userCheck = userRepository.existsByEmail(email);

        if(userCheck) throw new ApiException("user already exist login");
    }

    private User createUserEntity(RegistrationDto registrationDto){
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(getUserRole(registrationDto.getRole()));

        return user;
    }

    private Role getUserRole(String roleName){
        Optional<Role> role = roleRepository.findByName(roleName);

        if (role.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);

            return newRole;
        }

        return role.get();
    }
}