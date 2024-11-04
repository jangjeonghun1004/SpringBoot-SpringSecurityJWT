package com.example.demo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 접근 거부 시 상태 코드를 403으로 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ObjectMapper objectMapper = new ObjectMapper();
        CustomErrorResponseFormat customErrorResponseFormat = new CustomErrorResponseFormat();
        customErrorResponseFormat.setCode(403);
        customErrorResponseFormat.setError("Access Denied");
        customErrorResponseFormat.setMessage("접근 권한이 없습니다.");

        // JSON 형식으로 메시지를 반환하거나 다른 처리를 수행할 수 있음
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(customErrorResponseFormat));
    }
}
