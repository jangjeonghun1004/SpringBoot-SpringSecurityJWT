package com.example.demo.service;

import com.example.demo.common.CommonResponse;
import com.example.demo.config.security.JwtTokenProvider;
import com.example.demo.dto.SignInResultDTO;
import com.example.demo.dto.SignUpResultDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@RequiredArgsConstructor
@Service
public class SignServiceImpl implements SignService{
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @Override
    public SignUpResultDTO singUp(String id, String password, String name, String role) {
        User user;

        if(role.equalsIgnoreCase("admin")) {
            user = User.builder()
                    .uid(id)
                    .name(name)
                    .password(this.passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_ADMIN")).build();
        } else {
            user = User.builder()
                    .uid(id)
                    .name(name)
                    .password(this.passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER")).build();
        }

        User savedUser = this.userRepository.save(user);
        SignUpResultDTO signUpResultDTO = new SignUpResultDTO();

        if(StringUtils.isBlank(savedUser.getName())) {
            signUpResultDTO.setSuccess(false);
            signUpResultDTO.setCode(CommonResponse.FAIL.getCode());
            signUpResultDTO.setMsg(CommonResponse.FAIL.getMsg());
        } else {
            signUpResultDTO.setSuccess(true);
            signUpResultDTO.setCode(CommonResponse.SUCCESS.getCode());
            signUpResultDTO.setMsg(CommonResponse.SUCCESS.getMsg());
        }

        return signUpResultDTO;
    }

    @Override
    public SignInResultDTO signIn(String id, String password) throws RuntimeException {
        User user = this.userRepository.getByUid(id);

        if(!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException();
        }

        return SignInResultDTO.builder()
                .token(this.jwtTokenProvider.createToken(user.getUid(), user.getRoles()))
                .success(true)
                .code(CommonResponse.SUCCESS.getCode())
                .msg(CommonResponse.SUCCESS.getMsg())
                .build();
    }
}