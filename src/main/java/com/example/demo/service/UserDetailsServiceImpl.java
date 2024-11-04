package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService  {
    private final UserRepository userRepository;

    @Override
    public UserDetails getByUid(String id) throws UsernameNotFoundException {
        return this.userRepository.getByUid(id);
    }
}
