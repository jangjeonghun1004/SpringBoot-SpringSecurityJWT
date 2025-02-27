package com.example.demo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 인증 실패 시 상태 코드를 401으로 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();
        CustomErrorResponseFormat customErrorResponseFormat = new CustomErrorResponseFormat();
        customErrorResponseFormat.setCode(401);
        customErrorResponseFormat.setError("Authentication Failed");
        customErrorResponseFormat.setMessage("인증이 실패하였습니다.");

        // JSON 형식으로 메시지를 반환하거나 다른 처리를 수행할 수 있음
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(customErrorResponseFormat));
    }
}
