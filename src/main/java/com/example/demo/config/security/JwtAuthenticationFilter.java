package com.example.demo.config.security;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    /**
     * 사용자 인증을 처리합니다.
     *
     * @param httpServletRequest HttpServletRequest 객체
     * @param httpServletResponse HttpServletResponse 객체
     * @param filterChain FilterChain 객체
     * @throws ServletException 예외 처리
     * @throws IOException 에외 처리
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = this.jwtTokenProvider.resolveToken(httpServletRequest);
        if(!StringUtils.isBlank(token) && this.jwtTokenProvider.validateToken(token)) {
            // 사용자 자격증명
            Authentication authentication = this.jwtTokenProvider.getAuthentication(token);
            // 인증 처리 후 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
