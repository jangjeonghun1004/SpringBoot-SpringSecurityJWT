package com.example.demo.config.security;

import lombok.Data;

@Data
public class CustomErrorResponseFormat {
    private int code;
    private String error;
    private String message;
}
