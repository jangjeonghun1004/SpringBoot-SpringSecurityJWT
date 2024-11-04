package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    /**
     * 회원 정보를 가져옵니다.
     * @param id 아이디
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 예외 처리
     */
    UserDetails getByUid(String id) throws UsernameNotFoundException;
}
