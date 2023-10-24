package com.example.authenticationservice.controllers;

import com.example.authenticationservice.dtos.LoginDTO;
import com.example.authenticationservice.dtos.RegisterDTO;
import com.example.authenticationservice.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO);
        return ResponseEntity.ok().body(token);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/registerUser")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        authenticationService.registerUser(registerDTO);
        return ResponseEntity.status(201).build();
    }

}