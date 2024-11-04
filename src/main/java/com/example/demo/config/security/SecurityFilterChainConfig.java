package com.example.demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChainConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityFilterChainConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // REST API는 UI를 사용하지 않으므로 기본설정을 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // REST API는 csrf 보안이 필요 없으므로 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // JWT Token 인증 방식으로 세션은 필요 없으므로 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 사용자 요청에 대한 사용 권한 체크
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/sign/sign-in",
                                "/sign/sign-up",
                                "/sign/exception"
                        ).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        // product로 시작하는 Get 요청은 허용
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                        // 나머지 요청은 인증된 ADMIN만 접근 가능
                        .anyRequest().hasRole("ADMIN")
                )
                // JWT Token 필터를 id/password 인증 필터 이전에 추가
                .addFilterBefore(new JwtAuthenticationFilter(this.jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                // 예외 처리 설정
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return httpSecurity.build();
    }

}