package com.ato.backendapi.security.controller;

import com.ato.backendapi.security.dto.RequestResp;
import com.ato.backendapi.security.dto.SignInRequest;
import com.ato.backendapi.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<RequestResp> signUp(@RequestBody RequestResp signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<RequestResp> signIn(@RequestBody RequestResp signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<RequestResp> refreshToken(@RequestBody RequestResp refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}