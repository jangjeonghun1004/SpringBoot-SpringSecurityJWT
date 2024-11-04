package com.example.demo.service;

import com.example.demo.dto.SignInResultDTO;
import com.example.demo.dto.SignUpResultDTO;

public interface SignService {
    /**
     * 회원 가입을 합니다.
     * @param id 아이디
     * @param password 비밀번호
     * @param name 이름
     * @param role 권한
     * @return SignUpResultDTO 객체
     */
    SignUpResultDTO singUp(String id, String password, String name, String role);

    /**
     * 로그인을 합니다.
     * @param id 아이디
     * @param password 비밀번호
     * @return SignInResultDTO 객체
     * @throws RuntimeException 예외 처리
     */
    SignInResultDTO signIn(String id, String password) throws RuntimeException;
}
