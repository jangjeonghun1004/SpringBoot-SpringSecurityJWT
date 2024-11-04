package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 예제 13.30
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInResultDTO extends SignUpResultDTO {
    private String token;

    @Builder
    public SignInResultDTO(boolean success, int code, String msg, String token) {
        super(success, code, msg);
        this.token = token;
    }
}