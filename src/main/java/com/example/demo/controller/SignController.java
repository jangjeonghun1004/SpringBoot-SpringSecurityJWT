package com.example.demo.controller;

import com.example.demo.dto.SignInResultDTO;
import com.example.demo.dto.SignUpResultDTO;
import com.example.demo.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/sign")
public class SignController {
    private final SignService signService;


    @PostMapping("/sign-in")
    public SignInResultDTO signIn(
            @RequestParam String id,
            @RequestParam String password
    ) throws RuntimeException {
        return this.signService.signIn(id, password);
    }

    @PostMapping("sign-up")
    public SignUpResultDTO signUp(
            @RequestParam String id,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String role
    ) {
        return this.signService.singUp(id, password, name, role);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, httpHeaders, httpStatus);
    }

}
