package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class productController {

    @GetMapping()
    public ResponseEntity<String> getProduct() {
        return ResponseEntity.status(HttpStatus.OK).body("getProduct() 호출 완료.");
    }

    @Operation(summary = "Example endpoint", description = "Example endpoint with parameters")
    @Parameters ({@Parameter(name="X-AUTH-TOKEN", description = "로그인 성공 후 발급 받은 access_token", required = true, in = ParameterIn.HEADER)})
    @PostMapping()
    public ResponseEntity<String> createProduct(
            @Parameter(description = "The first parameter") @RequestParam String param1,
            @Parameter(description = "The second parameter") @RequestParam(required = false) String param2
    ) {
        return ResponseEntity.status(HttpStatus.OK).body("createProduct() 호출 완료.");
    }

}
