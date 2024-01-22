package com.threeht.havenhotelapplication.controller;

import com.threeht.havenhotelapplication.request.AuthenticationRequest;
import com.threeht.havenhotelapplication.response.AuthenticationResponse;
import com.threeht.havenhotelapplication.service.AuthenticationService;
import com.threeht.havenhotelapplication.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response = service.register(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.status(response.getStatus()).body(response);

    }

}
